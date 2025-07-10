package core.network.extensions

import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import core.analytics.Log
import core.models.ApiCallFailure
import core.models.Response
import core.models.response.EmptyResponse


/**
 * Skip parsing error response for these status codes as response is known and won't be a valid json
 */
private val ignoredStatusCode = setOf(
    HttpStatusCode.NotFound,
    HttpStatusCode.InternalServerError,
    HttpStatusCode.NotImplemented,
    HttpStatusCode.BadGateway,
    HttpStatusCode.GatewayTimeout,
)

internal fun Exception.resolveToFailure(): ApiCallFailure = when (this) {
    is SocketTimeoutException,
    is ConnectTimeoutException,
        -> ApiCallFailure.NetworkFailure
    is ResponseException -> {
        response.takeIf { it.status !in ignoredStatusCode }
            ?.let { response.decodeForResponseError() }
            .let {
                ApiCallFailure.HTTPFailure(
                    status = it?.message ?: response.status.description
                )
            }
    }

    is SerializationException -> {
        Log.error(this, "Unable to serialize response")
        ApiCallFailure.HTTPFailure(this.message ?: "Unable to serialize response")
    }

    else -> {
        Log.error(this, "Something went south!!")
        this.handlePlatformError()
    }
}

expect fun Exception.handlePlatformError() : ApiCallFailure


private fun HttpResponse.decodeForResponseError(): Response<EmptyResponse?>? = runBlocking {
    runCatching { body<Response<EmptyResponse?>>() }
        .onFailure {
            Log.error(it, "Unable to decode response `${bodyAsText()}`")
        }.getOrNull()
}
