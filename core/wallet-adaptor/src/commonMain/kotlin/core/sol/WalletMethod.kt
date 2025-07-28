@file:OptIn(ExperimentalUnsignedTypes::class)

package core.sol

import com.github.kittinunf.result.Result

@Suppress("EnumEntryName")
enum class WalletMethodName {
  connect
}

sealed class WalletMethod(val methodName: WalletMethodName) {
  class Connect : WalletMethod(WalletMethodName.connect)
}

sealed interface WalletResponse {
  class Error(val errorCode: Int, val errorMessage: String?) : kotlin.Error(errorMessage), WalletResponse

  sealed class Success : WalletResponse {
    class Connect(val walletPublicKey: ByteArray, val session: String, val nonce: String) : Success()
  }
}

typealias WalletResult<T> = Result<T, WalletResponse.Error>

@Suppress("ConstPropertyName")
internal object WalletUrlQueryParams {
  const val dappEncryptionPublicKey = "dapp_encryption_public_key"
  const val phantomEncryptionPublicKey = "phantom_encryption_public_key"
  const val data = "data"
  const val nonce = "nonce"
  const val redirectLink = "redirect_link"
  const val appUrl = "app_url"

  const val errorCode = "errorCode"

  const val errorMessage = "errorMessage"
}

internal fun StringBuilder.queryParam(key: String, value: String) {
  if (!contains("?")) append("?")
  if (!endsWith("?") && !endsWith("&")) append("&")
  append("$key=${value}")
}


