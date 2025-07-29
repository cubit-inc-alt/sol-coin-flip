package core.features.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import core.common.inject
import core.common.injecting
import core.common.isDebugBuild
import core.sol.WalletAdaptor
import core.sol.WalletResponse
import core.sol.WalletResult


@Composable
fun WelcomeScreen() {
  val viewModel by inject<WelcomeViewModel>()
  val connectedAccount by viewModel.connectedAccount.collectAsStateWithLifecycle()

  Scaffold {
    Column(
      modifier = Modifier.padding(it).fillMaxSize(),
      verticalArrangement = Arrangement.Top,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {

      Spacer(modifier = Modifier.height(20.dp))

      connectedAccount?.also {
        Text(
          text = "Connected",
          color = Color.Blue,
          textAlign = TextAlign.Center
        )
      }

      Text(
        text = connectedAccount?.toBase58() ?: "Not Connected",
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(60.dp))

      connectedAccount?.let {
        Button(onClick = viewModel::flipCoin) {
          Text("Flip Coin", color = Color.White)
        }
      } ?: run {
        Button(onClick = viewModel::connect) {
          Text("Connect Phantom", color = Color.White)
        }
      }
    }
  }
}
