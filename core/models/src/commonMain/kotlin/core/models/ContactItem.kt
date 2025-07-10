package core.models

import kotlinx.serialization.Serializable

@Serializable
data class ContactItem(
    val name: String = "Australia",
    val code: String = "+61",
    val imageRes: String?,
    val value: String? = null
)


