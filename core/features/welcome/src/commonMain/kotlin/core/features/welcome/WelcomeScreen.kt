package core.features.welcome

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.core.uri.Uri
import androidx.core.uri.UriUtils
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.onSuccess
import core.sol.LocalWalletAdaptor
import foundation.metaplex.solanaeddsa.Keypair
import foundation.metaplex.solanaeddsa.SolanaEddsa
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch


@Composable
fun WelcomeScreen() {
  val walletHandler = LocalWalletAdaptor.current

  var result by remember { mutableStateOf<Any?>(null) }

  Scaffold {
    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center
    ) {

      result?.also {
        Text(it.toString())
      }

      Button(
        onClick = {
          walletHandler.connect {
            result = it
          }
        }
      ) {
        Text("Connect Wallet")
      }
    }
  }
}
