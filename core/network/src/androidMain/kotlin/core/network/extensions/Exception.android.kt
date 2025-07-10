package core.network.extensions

import core.models.ApiCallFailure

actual fun Exception.handlePlatformError(): ApiCallFailure {
    return this.message?.let { ApiCallFailure.HTTPFailure(it) }
        ?: ApiCallFailure.UnknownFailure
}
