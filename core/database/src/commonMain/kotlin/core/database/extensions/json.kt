package core.database.extensions

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val json = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
}

inline fun <reified T> T.toJsonString(): String = json.encodeToString(this)
inline fun <reified T> String.asJsonTo(): T = json.decodeFromString(this)
