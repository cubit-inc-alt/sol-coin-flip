package core.common

import org.koin.mp.KoinPlatformTools
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
inline fun <reified T : Any> inject() = KoinPlatformTools.defaultContext().get().inject<T>()
