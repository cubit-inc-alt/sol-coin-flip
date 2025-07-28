package core.features.welcome

import androidx.lifecycle.ViewModel
import com.github.kittinunf.result.onSuccess
import com.metaplex.signer.Signer
import core.common.inject
import core.sol.WalletAdaptor
import foundation.metaplex.base58.encodeToBase58String
import foundation.metaplex.rpc.RPC
import foundation.metaplex.solanapublickeys.PublicKey
import kotlinx.coroutines.flow.MutableStateFlow


class WalletSigner(override val publicKey: PublicKey) : Signer {
  override suspend fun signMessage(message: ByteArray): ByteArray {
    TODO("Not yet implemented")
  }
}

class WelcomeViewModel : ViewModel() {
  private var signer: Signer? = null

  val connectedAccount = MutableStateFlow<PublicKey?>(null)
  val rpcConnection by inject<RPC>()
  val walletAdaptor by inject<WalletAdaptor>()

  fun buildTransaction() {

  }

  fun connect() {
    walletAdaptor.connect { result ->
      result.onSuccess {
        val publicKey = PublicKey(it.walletPublicKey.encodeToBase58String())
        signer  = WalletSigner(publicKey)
        connectedAccount.value = publicKey
      }
    }
  }
}
