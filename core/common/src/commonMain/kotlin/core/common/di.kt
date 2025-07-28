package core.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.koin.mp.KoinPlatformTools
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
inline fun <reified T : Any> inject() = KoinPlatformTools.defaultContext().get().inject<T>()

@Composable
@HiddenFromObjC
inline fun <reified T : Any> injecting() = remember {
  KoinPlatformTools.defaultContext().get().inject<T>()
}
