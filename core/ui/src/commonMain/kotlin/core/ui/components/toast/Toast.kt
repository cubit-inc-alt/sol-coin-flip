package core.ui.components.toast

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.StringResource
import kotlin.time.Duration.Companion.seconds

@Immutable
class Toast(
    val message: String?,
    val type: Type,
    val duration: kotlin.time.Duration,
    val icon: ImageVector? = null,
    val textIcon: String? = null,
    val messageRes: StringResource? = null,
) {
    enum class Type {
        Message,
        Alert,
        Error,
    }

    enum class Duration(val value: kotlin.time.Duration) {
        SHORT(2.seconds),
        LONG(3.5.seconds),
    }
}
