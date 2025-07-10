package core.ui.components.toast

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import core.ui.delegates.stateOf
import core.ui.delegates.update


class ToastManager : ViewModel() {
    internal val toasts = stateOf<List<Toast>>(emptyList())

    internal fun removeToast(toast: Toast) = toasts.update { this - toast }

    fun postMessage(
        message: String,
        icon: ImageVector? = null,
        textIcon: String? = null,
        duration: Toast.Duration = Toast.Duration.SHORT,
    ) = post(Toast(message, Toast.Type.Message, duration.value, icon, textIcon))

    fun postAlert(
        message: String,
        icon: ImageVector? = null,
        textIcon: String? = null,
        duration: Toast.Duration = Toast.Duration.SHORT,
    ) = post(Toast(message, Toast.Type.Alert, duration.value, icon, textIcon))

    fun postError(
        message: String,
        icon: ImageVector? = null,
        textIcon: String? = null,
        duration: Toast.Duration = Toast.Duration.SHORT,
    ) = post(Toast(message, Toast.Type.Error, duration.value, icon, textIcon))

    fun post(toast: Toast) {
        val oldToast = toasts.value.firstNotNullOfOrNull {
            it.message == toast.message && it.icon == toast.icon && it.type == toast.type
        }

        if (oldToast == null) {
            toasts.update {
                this + toast
            }
        }
    }
}
