package app

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.ComposeUIViewController
import core.app.AppUI
import core.app.AppUIViewModel
import org.koin.core.context.startKoin
import core.app.di.appModule
import core.app.onAppStarted
import core.common.inject
import core.sol.WalletAdaptor

@Suppress("unused", "FunctionName")
fun MainViewController() = ComposeUIViewController(
  configure = {
    startKoin {
      modules(appModule())
    }
  }
) {

  LaunchedEffect(Unit) {
    onAppStarted()
  }

  val viewModel by inject<AppUIViewModel>()
  AppUI(viewModel)
}

object Core {
  val walletAdaptor by inject<WalletAdaptor>()
}
