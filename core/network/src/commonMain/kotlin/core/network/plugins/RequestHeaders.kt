package core.network.plugins

import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import kotlinx.uuid.UUID
import kotlinx.uuid.generateUUID
import core.network.deviceInfo



internal val DeviceHeaders
    get() = createClientPlugin("CustomHeaderPlugin") {

        val deviceInfo = deviceInfo()

        onRequest { request, _ ->
            request.headers {
                append(HttpHeaders.XRequestId, UUID.generateUUID().toString())
                append("X-Device-Model", deviceInfo)
            }
        }
    }
