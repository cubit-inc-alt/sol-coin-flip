package core.network

import platform.UIKit.UIDevice

actual fun deviceInfo(): String = with(UIDevice.currentDevice) {
    "$name $systemName $systemVersion"
}
