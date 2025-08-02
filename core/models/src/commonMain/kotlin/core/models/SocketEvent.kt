package core.models

import kotlinx.serialization.Serializable

@Serializable
data class SocketEvent<T>(
  val event: String,
  val data: T
)
