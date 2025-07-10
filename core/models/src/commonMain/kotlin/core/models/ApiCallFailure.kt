package core.models

sealed class ApiCallFailure(override val message: String) : Exception() {
    data object NetworkFailure : ApiCallFailure("Couldn't reach server")
    data object UnknownFailure : ApiCallFailure("Something went wrong")
    class HTTPFailure(val status: String) : ApiCallFailure(status)
}
