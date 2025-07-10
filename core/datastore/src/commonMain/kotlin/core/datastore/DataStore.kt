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
    var accessToken by deviceStore.value<String>(ACCESS_TOKEN)
    var refreshToken by deviceStore.value<String>(REFRESH_TOKEN)
    var isLoggedIn by deviceStore.value<Boolean?>(IS_LOGGED_IN, defaultValue = false)

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

    companion object {
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
        const val IS_LOGGED_IN = "isLoggedIn"

    }
}
