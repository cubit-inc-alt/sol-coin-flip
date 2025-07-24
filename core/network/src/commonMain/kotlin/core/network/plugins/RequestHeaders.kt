package core.network.plugins

import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import core.network.deviceInfo
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@OptIn(ExperimentalUuidApi::class)
internal val DeviceHeaders
    get() = createClientPlugin("CustomHeaderPlugin") {

        val deviceInfo = deviceInfo()

        onRequest { request, _ ->
            request.headers {
                append(HttpHeaders.XRequestId, Uuid.random().toString())
                append("X-Device-Model", deviceInfo)
            }
        }
    }
