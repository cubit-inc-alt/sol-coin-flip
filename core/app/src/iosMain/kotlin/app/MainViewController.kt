package app

import androidx.compose.ui.window.ComposeUIViewController
import core.app.AppUI
import core.app.AppUIViewModel
import org.koin.core.context.startKoin
import core.app.di.appModule
import core.app.onAppStarted
import core.app.walletAdaptor
import core.common.inject

@Suppress("unused", "FunctionName")
fun MainViewController() = ComposeUIViewController(
  configure = {
    startKoin {
      modules(appModule())
    }
  }
) {
  onAppStarted()
  val viewModel by inject<AppUIViewModel>()
  AppUI(viewModel)
}

object WalletAdaptor {
  val adaptor = walletAdaptor
}
