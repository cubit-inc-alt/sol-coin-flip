@file:OptIn(ExperimentalObjCRefinement::class)

package core.network.webSocket

import core.analytics.Log
import core.common.isDebugBuild
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.wss
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.Frame.Text
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import core.network.webSocket.ConnectionState.CONNECTED
import core.network.webSocket.ConnectionState.CONNECTING
import core.network.webSocket.ConnectionState.CONNECTION_FAILURE
import core.network.webSocket.ConnectionState.DISCONNECTED
import core.network.webSocket.ConnectionState.INITIAL
import kotlin.coroutines.coroutineContext
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@HiddenFromObjC
class Typed<T>(val value: T, val typeInfo: TypeInfo) {
  companion object {
    inline operator fun <reified T> invoke(value: T): Typed<T> = Typed(value, typeInfo<T>())
  }
}

@HiddenFromObjC
interface SocketConnection {
  val response: SharedFlow<String>
  val connectionState: StateFlow<ConnectionState>

  suspend fun connect(reconnectDelay: Duration = 5.seconds)

  fun <T> sendTyped(typed: Typed<T>)

  fun sendRaw(message: String)

  fun disconnect()

  fun connectionState(): ConnectionState = connectionState.value
}

@HiddenFromObjC
enum class ConnectionState {
  INITIAL,
  DISCONNECTED,
  CONNECTION_FAILURE,
  CONNECTING,
  CONNECTED,
}

@HiddenFromObjC
class KtorSocketConnection(
  private val tag: String,
  private val wssUrl: String,
  private val httpClient: HttpClient,
  private val autoConnect: Boolean = true
) : SocketConnection {
  private val _response = MutableSharedFlow<String>()
  override val response: SharedFlow<String> = _response

  private val _connectionState = MutableStateFlow(INITIAL)
  override val connectionState: StateFlow<ConnectionState> = _connectionState

  // queue for outgoing messages
  private var messages = Channel<Typed<*>>()

  // socket close requests
  private var closeRequests = Channel<Unit>()

  init {
    if (autoConnect) {
      CoroutineScope(Dispatchers.IO).launch { connect() }
    }
  }

  private suspend fun createNewConnection(
    url: String,
    onDisconnect: (e: Throwable?) -> Unit,
    onUnableToConnect: (e: Throwable) -> Unit,
    onConnected: () -> Unit,
  ) {
    runCatching {
      httpClient.wss(url) {
        onConnected()
        outgoing.invokeOnClose(onDisconnect)
        try {
          startSending()
          waitForCloseRequest()
          startReceiving()
        } catch (e: ClosedReceiveChannelException) {
          onDisconnect(e)
        }
      }
    }.onFailure(onUnableToConnect)
  }

  private var connectionJob: Job? = null

  override suspend fun connect(reconnectDelay: Duration) {
    val currentState = connectionState()

    if (currentState == CONNECTED) {
      return Log.info(tag, "skipping new connecting socket is already '$currentState'")
    }

    connectionJob?.cancel()
    connectionJob = CoroutineScope(coroutineContext + Dispatchers.IO).launch {
      connectAndRetry(reconnectDelay)
    }
  }

  /**
   * Creates a new connection that is kept alive till the calling coroutine scope lives
   */
  private suspend fun CoroutineScope.connectAndRetry(reconnectDelay: Duration) {
    messages = Channel()
    closeRequests = Channel()

    var connectionError: Throwable? = null

    while (isActive) {
      _connectionState.value = CONNECTING
      Log.info(tag, "Connecting...")

      createNewConnection(
        url = wssUrl,
        onConnected = ::onConnected,
        onDisconnect = {
          connectionError = it
        },
        onUnableToConnect = {
          connectionError = it
        },
      )

      val wasConnected = connectionState() == CONNECTED

      if (wasConnected) {
        onSocketDisconnected(connectionError)
      } else {
        onUnableToConnect(connectionError)
      }

      // coroutine scope was cancelled or don't want to retry
      if (connectionError is CancellationException || reconnectDelay <= 0.seconds) {
        break
      }

      Log.info(tag, "will retry to connect after $reconnectDelay")
      delay(reconnectDelay)
    }

    messages.close()
    closeRequests.close()

    onTerminated()
  }

  private suspend fun DefaultClientWebSocketSession.startReceiving() {
    while (true) {
      val frame: Frame = incoming.receive()
      Log.info(tag, "Message on socket '$frame'")

      if (frame is Text) {
        frame.readText().also { text ->
          _response.emit(text)
        }
      }
    }
  }

  @OptIn(DelicateCoroutinesApi::class)
  private fun DefaultClientWebSocketSession.startSending() = launch {
    while (!messages.isClosedForReceive) {
      val request = messages.receive()
      sendSerialized(request.value, request.typeInfo)
    }
  }

  private fun DefaultClientWebSocketSession.waitForCloseRequest() = launch {
    closeRequests.receive()
    close(CloseReason(CloseReason.Codes.NORMAL, "disconnecting"))
  }

  private fun onConnected() {
    Log.info(tag, "Socket connected !!")
    _connectionState.value = CONNECTED
  }

  private fun onUnableToConnect(connectionError: Throwable?) {
    Log.info(tag, "Unable to connect $connectionError")
    _connectionState.value = CONNECTED
  }

  private fun onSocketDisconnected(throwable: Throwable?) {
    if (isDebugBuild()) {
      throwable?.printStackTrace()
    }
    Log.info(tag, "Socket disconnected $throwable")
    _connectionState.value = DISCONNECTED
  }

  private fun onTerminated() {
    Log.info(tag, "Socket terminated!!")
  }

  override fun <T> sendTyped(typed: Typed<T>) {
    val currentState = connectionState()
    if (currentState != CONNECTED && currentState != CONNECTING) {
      return Log.info(tag, "Socket is '$currentState', unable to send message '${typed.value}'")
    }

    val result = messages.trySendBlocking(typed)

    if (!result.isSuccess) {
      Log.info(tag, "Unable to send message '${typed.value}' $result")
    }
  }

  override fun sendRaw(message: String) {
    sendTyped(Typed(message))
  }

  override fun disconnect() {
    val currentState = connectionState()
    if (currentState == DISCONNECTED || currentState == CONNECTION_FAILURE) {
      return Log.debug("skipping disconnect, socket is already '$currentState'")
    } else {
      closeRequests.trySend(Unit)
    }
  }
}

inline fun <reified T> SocketConnection.send(message: T) = sendTyped(Typed(message))
