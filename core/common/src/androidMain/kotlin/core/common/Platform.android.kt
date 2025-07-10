package core.common

import core.common.BuildConfig


actual fun platform() = Platform.Android

actual fun isDebugBuild(): Boolean = BuildConfig.DEBUG
