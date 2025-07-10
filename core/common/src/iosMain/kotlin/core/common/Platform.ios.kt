package core.common

import core.common.Platform
import kotlin.experimental.ExperimentalNativeApi

actual fun platform(): Platform = Platform.IOS

@OptIn(ExperimentalNativeApi::class)
actual fun isDebugBuild(): Boolean = kotlin.native.Platform.isDebugBinary
