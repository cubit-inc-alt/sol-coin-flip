package core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin

internal actual fun baseHttpClient() = HttpClient(Darwin)
