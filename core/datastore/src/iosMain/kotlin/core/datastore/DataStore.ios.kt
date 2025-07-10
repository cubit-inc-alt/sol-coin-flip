package core.datastore

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.ObservableSettings
import platform.Foundation.NSBundle
import platform.Foundation.NSUserDefaults

internal actual fun deviceDataStore(): ObservableSettings {
    val name = "${NSBundle.mainBundle.bundleIdentifier}.${Constants.DEVICE_PREFERENCES}"

    return NSUserDefaultsSettings(NSUserDefaults(suiteName = name))
}

internal actual fun userDataStore(): ObservableSettings {
    val name = "${NSBundle.mainBundle.bundleIdentifier}.${Constants.USER_PREFERENCES}"

    return NSUserDefaultsSettings(NSUserDefaults(suiteName = name))
}
