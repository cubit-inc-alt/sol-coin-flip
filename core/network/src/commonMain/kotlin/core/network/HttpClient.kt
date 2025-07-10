package core.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import core.common.PandaConfig
import core.common.inject
import core.common.isDebugBuild
import core.datastore.DataStore
import core.network.extensions.getBearerTokens
import core.network.plugins.DeviceHeaders
import core.analytics.Log
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds


internal fun buildAuthHttpClient(
    baseHttpClient: HttpClient,
    dataStore: DataStore
): HttpClient = baseHttpClient.config {
    expectSuccess = true

    installLogging()

    installDefaultRequest()

    installJsonContentNegotiation()

    installTimeout()

    installAuth()
    with(dataStore) {
        install(DeviceHeaders)
    }
}


internal fun buildNoAuthHttpClient(
    httpClient: HttpClient
): HttpClient = httpClient.config {
    expectSuccess = true

    installLogging()

    installWebSockets()

    installTimeout()
}

internal fun HttpClientConfig<*>.installWebSockets() {
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(nonStrictJson)
    }
}

internal fun HttpClientConfig<*>.installDefaultRequest() {
    install(DefaultRequest) {
        url(PandaConfig.environment)
    }
}

internal fun HttpClientConfig<*>.installJsonContentNegotiation() {
    install(ContentNegotiation) {
        json(nonStrictJson)
    }
}

internal fun HttpClientConfig<*>.installTimeout() {
    install(HttpTimeout) {
        socketTimeoutMillis = 5.minutes.inWholeMilliseconds
        connectTimeoutMillis = 30.seconds.inWholeMilliseconds
    }
}

internal fun HttpClientConfig<*>.installLogging() {
    install(Logging) {
        level = if (isDebugBuild()) LogLevel.ALL else LogLevel.INFO

        this.logger = object : Logger {
            override fun log(message: String) {
                Log.info("Ktor", message)
            }
        }
    }
}

internal fun HttpClientConfig<*>.installAuth() {
    val tokenRefresher by inject<BearerTokenRefresher>()
    val dataStore by inject<DataStore>()
    install(Auth) {
        bearer {
            loadTokens { dataStore.getBearerTokens() }
            refreshTokens {
                if (this.oldTokens != dataStore.getBearerTokens()) {
                    loadTokens { dataStore.getBearerTokens() }
                }

                tokenRefresher.refreshToken {
                    markAsRefreshTokenRequest()
                }
            }
        }
    }
}


internal expect fun baseHttpClient(): HttpClient
