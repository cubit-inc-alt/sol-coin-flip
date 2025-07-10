package core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@Entity
data class TestItemEntity(
    @PrimaryKey(autoGenerate = false) @SerialName("id") val id: String,
    @SerialName("value") val value: String,
)
