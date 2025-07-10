package core.network.extensions

import io.ktor.client.engine.darwin.DarwinHttpRequestException
import core.models.ApiCallFailure

actual fun Exception.handlePlatformError(): ApiCallFailure {
    return when (this) {
        is DarwinHttpRequestException -> {
            ApiCallFailure.HTTPFailure(this.origin.localizedDescription)
        }

        else -> this.message?.let { ApiCallFailure.HTTPFailure(it) }
            ?: ApiCallFailure.UnknownFailure
    }
}
