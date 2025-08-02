package core.data.repository

import com.github.kittinunf.result.onSuccess
import core.common.inject
import core.database.extensions.asJsonTo
import core.database.extensions.json
import core.models.CoinFlip
import core.models.SocketEvent
import core.models.local.Play
import core.network.RemoteApi
import core.network.webSocket.SocketConnection
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import org.koin.core.KoinApplication.Companion.init

@Suppress("ConstPropertyName")
object Event {
  const val devNetFlip = "DEV_NET_FLIP"
  const val mainNetFlip = "MAIN_NET_FLIP"
}

class PlayRepository() {

  val remoteApi by inject<RemoteApi>()

  private
  val socketConnection by inject<SocketConnection>()

  val plays = MutableStateFlow<Set<CoinFlip>>(emptySet())
  val newPlayEvents = Channel<CoinFlip>()

  init {
    listenForEvents()
  }

  @OptIn(DelicateCoroutinesApi::class)
  private fun listenForEvents() = GlobalScope.launch(Dispatchers.IO) {
    socketConnection.response.collect(::onNewData)
  }

  private fun onNewData(dataString: String) = runCatching {
    val data = json.parseToJsonElement(dataString) as JsonObject
    val event = (data["event"] as? JsonPrimitive)?.contentOrNull ?: return@runCatching

    when (event) {
      "test",
      Event.mainNetFlip,
      Event.devNetFlip -> {
        val flipEvent = dataString.asJsonTo<SocketEvent<CoinFlip>>()
        plays.value = plays.value + flipEvent.data

        newPlayEvents.trySend(flipEvent.data)
      }

      else -> println("unknown event $event")
    }
  }

  suspend fun refreshPlays() = withContext(Dispatchers.IO) {
    remoteApi.getPlays()
      .onSuccess {
        plays.value = it.data.toSet()
      }
  }

  fun getPlayDataList(): List<Play> {
    return listOf(
      Play(
        id = "05be5f6g78h",
        dateTime = "1 minute ago",
        wager = +7.0f,
        desc = "shifted 6.1 and subtracted two",
        achievement = "8,456 XP"
      ),
      Play(
        id = "a1b2c3d4e5f",
        dateTime = "5 minutes ago",
        wager = -5.5f,
        desc = "flipped twice and gained momentum",
        achievement = "6,123 XP"
      ),
      Play(
        id = "f9e8d7c6b5a",
        dateTime = "10 minutes ago",
        wager = -2.2f,
        desc = "lost after delay, missed trigger",
        achievement = "3,678 XP"
      ),
      Play(
        id = "112233445566",
        dateTime = "20 minutes ago",
        wager = +10.0f,
        desc = "double wager, no shift",
        achievement = "10,789 XP"
      ),
      Play(
        id = "77889900aabb",
        dateTime = "30 minutes ago",
        wager = +3.3f,
        desc = "shifted 1.5 and rebounded",
        achievement = "5,432 XP"
      )
    )
  }
}
