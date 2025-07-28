package app.android

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import core.app.AppUI
import core.app.AppUIViewModel
import core.app.walletAdaptor
import core.common.inject


class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val viewModel by inject<AppUIViewModel>()
      AppUI(viewModel)
    }

    intent.checkForWalletReturnData()
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    setIntent(intent)
    intent.checkForWalletReturnData()
  }
}

private fun Intent.checkForWalletReturnData() {
  val data = data ?: return

  walletAdaptor.handleReturnFromWallet(data.toString())
}
