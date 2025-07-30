package core.sol

import androidx.compose.ui.platform.UriHandler
import androidx.core.uri.Uri
import androidx.core.uri.UriUtils
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.map
import core.database.extensions.json
import core.sol.WalletConnectionStatus.*
import core.sol.WalletResponse.Success.*
import core.sol.nacl.NaClLowLevel
import core.sol.nacl.Nacl
import foundation.metaplex.base58.decodeBase58
import foundation.metaplex.base58.encodeToBase58String
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.time.Duration.Companion.minutes
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@Suppress("ConstPropertyName")
object PhantomWalletInfo {
  const val baseUrl = "https://phantom.app/ul/v1/"
}


@Suppress("SpellCheckingInspection")
enum class NetworkCluster(val id: String) {
  MainnetBeta("mainnet-beta"),
  Testnet("testnet"),
  Devnet("devnet"),
}

sealed class WalletConnectionStatus {
  class Connected(
    val sharedSecret: ByteArray,
    val session: String,
    val walletPublicKey: ByteArray,
    val userAccountPublicKey: String
  ) : WalletConnectionStatus()

  object Disconnected : WalletConnectionStatus()
}

@OptIn(ExperimentalUuidApi::class, ExperimentalUnsignedTypes::class)
class WalletAdaptor(
  private val appUrl: String = "https://cubit.com.np",
  private val redirectLinkPrefix: String = "flipper://wallet"
) {

  var connectionStatus: WalletConnectionStatus = Disconnected

  lateinit var urlHandler: UriHandler

  private lateinit var dAppKeyPair: Nacl.KeyPair
  private val callBacks = mutableMapOf<Uuid, (result: WalletResult<WalletResponse.Success>) -> Unit>()

  init {
    CoroutineScope(Dispatchers.IO).launch {
      dAppKeyPair = Nacl.Box.keyPair()
    }
  }

  private fun Uri.decodeWalletResponse(): WalletResponse {
    queryParam(WalletUrlQueryParams.errorCode)?.let { errorCode ->
      return WalletResponse.Error(
        errorCode = errorCode.toIntOrNull() ?: -1,
        errorMessage = queryParam(WalletUrlQueryParams.errorMessage),
      )
    }

    val (method, _) = getPathSegments()
      .filter { it.isNotBlank() }
      .takeIf { it.size >= 2 } ?: return parseError("Not enough path segments")

    val methodEncoded = enumValueOf<WalletMethodName>(method)

    val nonce = queryParam(WalletUrlQueryParams.nonce)?.decodeBase58()
      ?: return parseError("unable to decode nonce")

    val boxedMessage = queryParam(WalletUrlQueryParams.data)?.decodeBase58()
      ?: return parseError("unable to decode data")

    return when (methodEncoded) {
      WalletMethodName.connect -> {
        val walletPublicKey = queryParam(WalletUrlQueryParams.phantomEncryptionPublicKey)
          ?.decodeBase58()
          ?: return parseError("unable to decode ${WalletUrlQueryParams.phantomEncryptionPublicKey}")

        val sharedSecret = sharedSecretOrNull(dAppKeyPair.secretKey, walletPublicKey)
          ?: return parseError("unable to decode data")

        val data = decryptPayloadOrNull(sharedSecret = sharedSecret, message = boxedMessage, nonce = nonce)?.let {
          Json.parseToJsonElement(it.decodeToString()) as JsonObject
        } ?: return parseError("Unable to decode data")

        val session = (data["session"] as? JsonPrimitive)?.content ?: return parseError("Unable to decode data")
        val userAccountPublicKey =
          (data["public_key"] as? JsonPrimitive)?.content ?: return parseError("Unable to decode data")

        connectionStatus = Connected(
          sharedSecret = sharedSecret,
          session = session,
          userAccountPublicKey = userAccountPublicKey,
          walletPublicKey = walletPublicKey
        )

        Connect(userWalletPublicKey = userAccountPublicKey)
      }

      WalletMethodName.signTransaction -> {
        val connectionStatus = connectionStatus.ensureConnected()

        val data = decryptPayloadOrNull(
          sharedSecret = connectionStatus.sharedSecret,
          message = boxedMessage,
          nonce = nonce
        )?.let {
          Json.parseToJsonElement(it.decodeToString()) as JsonObject
        } ?: return parseError("Unable to decode data")

        val transaction = (data["transaction"] as? JsonPrimitive)?.content ?: return parseError("transaction missing")

        SignTransaction(transaction.decodeBase58())
      }

      WalletMethodName.signMessage -> {

        val connectionStatus = connectionStatus.ensureConnected()

        val data = decryptPayloadOrNull(
          sharedSecret = connectionStatus.sharedSecret,
          message = boxedMessage,
          nonce = nonce
        )?.let {
          Json.parseToJsonElement(it.decodeToString()) as JsonObject
        } ?: return parseError("Unable to decode data")

        val signature = (data["signature"] as? JsonPrimitive)?.content ?: return parseError("signature is missing")

        SignMessage(signature.decodeBase58())
      }
    }
  }

  @Suppress("UNCHECKED_CAST")
  @OptIn(ExperimentalUuidApi::class)
  fun <Result : WalletResponse.Success> invoke(
    method: WalletMethod<Result>,
    callBack: (WalletResult<Result>) -> Unit
  ) {
    val uuid = Uuid.random()
    callBacks[uuid] = callBack as (WalletResult<WalletResponse.Success>) -> Unit
    val url = method.encode(connectionStatus, dAppKeyPair, uuid, redirectLinkPrefix, appUrl)
    urlHandler.openUri(url)
  }

  suspend fun <Result : WalletResponse.Success> invoke(
    method: WalletMethod<Result>
  ) = suspendCoroutine<WalletResult<Result>> { continuation ->
    invoke(method, continuation::resume)
  }

  fun connect(args: WalletMethod.Connect, onResult: (WalletResult<Connect>) -> Unit) = invoke(args, onResult)
  suspend fun connect(args: WalletMethod.Connect) = invoke(args)

  fun signTransaction(args: WalletMethod.SignTransaction, onResult: (WalletResult<SignTransaction>) -> Unit) =
    invoke(args, onResult)

  suspend fun signTransaction(args: WalletMethod.SignTransaction) = invoke(args)

  fun signMessage(args: WalletMethod.SignMessage, onResult: (WalletResult<SignMessage>) -> Unit) =
    invoke(args, onResult)

  suspend fun signMessage(args: WalletMethod.SignMessage) = invoke(args)

  fun handleReturnFromWallet(url: String) {
    if (!url.startsWith(redirectLinkPrefix))
      return

    val uri = UriUtils.parse(url)

    val (method, requestId) = uri.getPathSegments().filter { it.isNotBlank() }.takeIf { it.size >= 2 }
      ?: return

    val id = Uuid.parse(requestId)

    val param = uri.decodeWalletResponse()

    val result = when (param) {
      is WalletResponse.Success -> Result.success(param)
      is WalletResponse.Error -> Result.failure(param)
    }

    callBacks.remove(id)?.invoke(result)
  }
}

private fun WalletConnectionStatus.ensureConnected() = this as? Connected ?: error("wallet is not connected")

@OptIn(ExperimentalUuidApi::class, ExperimentalUnsignedTypes::class)
private fun WalletMethod<*>.encode(
  connectionStatus: WalletConnectionStatus,
  dAppKeyPair: Nacl.KeyPair,
  requestId: Uuid,
  redirectLinkPrefix: String,
  appUrl: String
): String {

  return buildString {
    append(PhantomWalletInfo.baseUrl)
    append("$methodName?")

    when (this@encode) {
      is WalletMethod.Connect -> {
        queryParam(WalletUrlQueryParams.dappEncryptionPublicKey, dAppKeyPair.publicKey.encodeToBase58String())
        queryParam(WalletUrlQueryParams.cluster, cluster.id)
      }

      is WalletMethod.SignTransaction -> {

        val connection = connectionStatus.ensureConnected()

        val payload = buildJsonObject {
          put("session", connection.session)
          put("transaction", this@encode.content.encodeToBase58String())
        }

        val (nonce, encryptedPayload) = encryptPayloadOrNull(
          payload = json.encodeToString(payload).encodeToByteArray(),
          sharedSecret = connection.sharedSecret
        )

        queryParam(WalletUrlQueryParams.dappEncryptionPublicKey, dAppKeyPair.publicKey.encodeToBase58String())
        queryParam(WalletUrlQueryParams.nonce, nonce.encodeToBase58String())
        queryParam(WalletUrlQueryParams.payload, encryptedPayload.encodeToBase58String())
      }

      is WalletMethod.SignMessage -> {
        val connection = connectionStatus.ensureConnected()

        val payload = buildJsonObject {
          put("session", connection.session)
          put("message", this@encode.content.encodeToBase58String())
        }

        val (nonce, encryptedPayload) = encryptPayloadOrNull(
          payload = json.encodeToString(payload).encodeToByteArray(),
          sharedSecret = connection.sharedSecret
        )

        queryParam(WalletUrlQueryParams.dappEncryptionPublicKey, dAppKeyPair.publicKey.encodeToBase58String())
        queryParam(WalletUrlQueryParams.nonce, nonce.encodeToBase58String())
        queryParam(WalletUrlQueryParams.payload, encryptedPayload.encodeToBase58String())
      }
    }

    queryParam(WalletUrlQueryParams.appUrl, appUrl)
    queryParam(WalletUrlQueryParams.redirectLink, "$redirectLinkPrefix/$methodName/$requestId")
  }
}

private fun sharedSecretOrNull(dAppPrivateKey: ByteArray, walletPublicKey: ByteArray) = runCatching {
  Nacl.Box.before(walletPublicKey, dAppPrivateKey)
}.getOrNull()

private fun encryptPayloadOrNull(payload: ByteArray, sharedSecret: ByteArray): Pair<ByteArray, ByteArray> {
  val nonce = NaClLowLevel.randombytes(24)
  return nonce to Nacl.Secretbox.seal(payload, nonce, sharedSecret)
}

fun decryptPayloadOrNull(
  sharedSecret: ByteArray,
  message: ByteArray,
  nonce: ByteArray
) = runCatching {
  Nacl.Secretbox.open(box = message, nonce = nonce, key = sharedSecret)
}.getOrNull()

private fun Uri.parseError(message: String): WalletResponse.Error {
  return WalletResponse.Error(-1, "Unable to parse response message: $message, uri:$this")
}
