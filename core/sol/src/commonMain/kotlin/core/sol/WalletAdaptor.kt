package core.sol

import androidx.compose.ui.platform.UriHandler
import androidx.core.uri.Uri
import androidx.core.uri.UriUtils
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.map
import core.sol.nacl.Nacl
import foundation.metaplex.base58.decodeBase58
import foundation.metaplex.base58.encodeToBase58String
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@Suppress("ConstPropertyName")
object PhantomWalletInfo {
  const val baseUrl = "https://phantom.app/ul/v1/"
}

@OptIn(ExperimentalUuidApi::class, ExperimentalUnsignedTypes::class)
class WalletAdaptor(
  private val appUrl: String = "https://cubit.com.np",
  private val redirectLinkPrefix: String = "flipper://wallet"
) {
  lateinit var urlHandler: UriHandler
  lateinit var walletPublicKey: ByteArray
  private lateinit var dAppKeyPair: Nacl.KeyPair
  private val callBacks = mutableMapOf<Uuid, (result: WalletResult<WalletResponse.Success>) -> Unit>()

  init {
    CoroutineScope(Dispatchers.IO).launch {
      dAppKeyPair = Nacl.Box.keyPair()
    }
  }

  @OptIn(ExperimentalUuidApi::class, ExperimentalUnsignedTypes::class)
  fun WalletMethod.encode(
    requestId: Uuid
  ): String {

    return buildString {
      append(PhantomWalletInfo.baseUrl)
      append("$methodName?")

      when (this@encode) {
        is WalletMethod.Connect -> {
          queryParam(
            WalletUrlQueryParams.dappEncryptionPublicKey,
            dAppKeyPair.publicKey.encodeToBase58String()
          )
        }
      }

      queryParam(WalletUrlQueryParams.appUrl, appUrl)
      queryParam(WalletUrlQueryParams.redirectLink, "$redirectLinkPrefix/$methodName/$requestId")
    }
  }

  fun Uri.decodeWalletResponse(): WalletResponse {

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

    walletPublicKey = queryParam(WalletUrlQueryParams.phantomEncryptionPublicKey)
      ?.decodeBase58()
      ?: return parseError("unable to decode ${WalletUrlQueryParams.phantomEncryptionPublicKey}")

    val nonce = queryParam(WalletUrlQueryParams.nonce)?.decodeBase58()
      ?: return parseError("unable to decode nonce")

    return when (methodEncoded) {
      WalletMethodName.connect -> {
        val boxedMessage = queryParam(WalletUrlQueryParams.data)?.decodeBase58()
          ?: return parseError("unable to decode data")

        val data = openMessage(boxedMessage, nonce)
          .also {
            println(
              """
              message: ${queryParam(WalletUrlQueryParams.data)},

            """.trimIndent()
            )
          }?.let {
            Json.parseToJsonElement(it.decodeToString()) as JsonObject
          } ?: return parseError("Unable to decode data")

        WalletResponse.Success.Connect(
          walletPublicKey.encodeToBase58String(),
          session = data["session"]?.toString() ?: return parseError("Unable to decode data"),
          nonce = nonce.encodeToBase58String()
        )
      }
    }
  }

  private fun openMessage(
    message: ByteArray,
    nonce: ByteArray
  ) = runCatching {
    Nacl.Box.open(
      msg = message,
      nonce = nonce,
      publicKey = walletPublicKey,
      secretKey = dAppKeyPair.secretKey
    )
  }.getOrNull()

  @OptIn(ExperimentalUuidApi::class)
  fun invoke(method: WalletMethod, callBack: (WalletResult<WalletResponse.Success>) -> Unit) {
    val uuid = Uuid.random()
    callBacks[uuid] = callBack
    urlHandler.openUri(method.encode(uuid))
  }

  fun connect(onConnected: (WalletResult<WalletResponse.Success.Connect>) -> Unit) {
    invoke(WalletMethod.Connect()) {
      val result = it.map { walletCallbackSuccess -> walletCallbackSuccess as WalletResponse.Success.Connect }
      onConnected(result)
    }
  }

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


private fun Uri.parseError(message: String): WalletResponse.Error {
  return WalletResponse.Error(-1, "Unable to parse response message: $message uri:$this")
}
