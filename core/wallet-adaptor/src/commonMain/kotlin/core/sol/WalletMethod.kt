@file:OptIn(ExperimentalUnsignedTypes::class)

package core.sol

import com.github.kittinunf.result.Result

@Suppress("EnumEntryName")
enum class WalletMethodName {
  connect,
  signTransaction,
  signMessage
}

sealed class WalletMethod<Result : WalletResponse.Success>(val methodName: WalletMethodName) {
  class Connect(val cluster: NetworkCluster) : WalletMethod<WalletResponse.Success.Connect>(WalletMethodName.connect)
  class SignTransaction(val content: ByteArray) : WalletMethod<WalletResponse.Success.SignTransaction>(WalletMethodName.signTransaction)
  class SignMessage(val content: ByteArray) : WalletMethod<WalletResponse.Success.SignMessage>(WalletMethodName.signMessage)
}

sealed interface WalletResponse {
  class Error(val errorCode: Int, val errorMessage: String?) : kotlin.Error(errorMessage), WalletResponse

  sealed class Success : WalletResponse {
    class Connect(val userWalletPublicKey: String) : Success()
    class SignTransaction(val transaction: ByteArray) : Success()
    class SignMessage(val signature: ByteArray) : Success()
  }
}

typealias WalletResult<T> = Result<T, WalletResponse.Error>

@Suppress("ConstPropertyName")
internal object WalletUrlQueryParams {
  const val dappEncryptionPublicKey = "dapp_encryption_public_key"
  const val phantomEncryptionPublicKey = "phantom_encryption_public_key"
  const val data = "data"
  const val nonce = "nonce"
  const val payload = "payload"
  const val redirectLink = "redirect_link"
  const val appUrl = "app_url"
  const val cluster = "cluster"

  const val errorCode = "errorCode"

  const val errorMessage = "errorMessage"
}

internal fun StringBuilder.queryParam(key: String, value: String) {
  if (!contains("?")) append("?")
  if (!endsWith("?") && !endsWith("&")) append("&")
  append("$key=${value}")
}


