package app.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import core.app.AppUI
import core.app.AppUIViewModel
import core.common.inject
import core.sol.WalletAdaptor


class MainActivity : ComponentActivity() {

  val walletAdaptor: WalletAdaptor by inject<WalletAdaptor>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
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

  private fun Intent.checkForWalletReturnData() {
    val data = data ?: return

    walletAdaptor.handleReturnFromWallet(data.toString())
  }

}

