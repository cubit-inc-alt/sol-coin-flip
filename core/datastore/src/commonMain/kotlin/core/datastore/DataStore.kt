package core.datastore

import com.russhwolf.settings.ObservableSettings
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

internal expect fun deviceDataStore(): ObservableSettings

internal expect fun userDataStore(): ObservableSettings


class DataStore(
  private val deviceStore: ObservableSettings = deviceDataStore(),
  private val userStore: ObservableSettings = userDataStore(),
) {
  var accessToken by deviceStore.value<String?>(Companion.accessToken)
  var refreshToken by deviceStore.value<String?>(Companion.refreshToken)
  var isLoggedIn by deviceStore.value<Boolean?>(Companion.isLoggedIn, defaultValue = false)

  var termsAccepted by userStore.value(Companion.termsAccepted, false)
  var sharedSecret by userStore.value<String?>(Companion.sharedSecret)
  var session by userStore.value<String?>(Companion.session)
  var userAccountPublicKey by userStore.value<String?>(Companion.userAccountPublicKey)
  var dAppPrivateKey by userStore.value<String?>(Companion.dAppPrivateKey)
  var dAppPublicKey by userStore.value<String?>(Companion.dAppPublicKey)

  fun clearUserData() {
    userStore.clear()
    deviceStore.clear()
  }

  fun getBooleanFlow(key: String): Flow<Boolean> =
    callbackFlow {
      val listener =
        deviceStore.addBooleanListener(key, defaultValue = false) { value ->
          trySend(value)
        }

      // Emit the initial value
      trySend(deviceStore.getBoolean(key, false))

      awaitClose { listener.deactivate() }
    }

  @Suppress("ConstPropertyName")
  companion object {
    private const val sharedSecret = "walletConnectionSharedSecret"
    private const val session = "walletSession"
    private const val userAccountPublicKey = "userAccountPublicKey"
    private const val dAppPrivateKey = "dAppPrivateKey"
    private const val dAppPublicKey = "dAppPublicKey"
    private const val termsAccepted = "termsAccepted"


    private const val accessToken = "accessToken"

    private const val refreshToken = "refreshToken"
    const val isLoggedIn = "isLoggedIn"

  }
}
