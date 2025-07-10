package core.network

import android.os.Build

actual fun deviceInfo(): String {
    return "${Build.DEVICE} ${Build.MODEL}"
}
