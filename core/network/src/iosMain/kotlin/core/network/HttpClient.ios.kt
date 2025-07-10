package core.network

import io.ktor.client.HttpClient

internal actual fun baseHttpClient() = HttpClient()
