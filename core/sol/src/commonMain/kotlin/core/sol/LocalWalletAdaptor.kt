package core.sol

import androidx.compose.runtime.staticCompositionLocalOf

val LocalWalletAdaptor = staticCompositionLocalOf<WalletAdaptor> {
  error("CompositionLocal LocalWalletAdaptorProvider not present")
}
