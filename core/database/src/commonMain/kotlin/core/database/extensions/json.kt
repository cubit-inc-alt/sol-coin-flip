package core.database.extensions

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val json = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
}

inline fun <reified T> T.toJson(): String = json.encodeToString(this)
inline fun <reified T> String.to(): T = json.decodeFromString(this)
