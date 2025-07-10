package core.network

import kotlinx.serialization.json.Json

internal val nonStrictJson = Json {
  ignoreUnknownKeys = true
  explicitNulls = false
  encodeDefaults = true
}
