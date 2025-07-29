package core.features.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.result.onSuccess
import com.metaplex.signer.Signer
import core.common.inject
import core.sol.WalletAdaptor
import core.web3.coinFlip.CoinFlipProgram

import foundation.metaplex.base58.encodeToBase58String
import foundation.metaplex.rpc.Commitment
import foundation.metaplex.rpc.RPC
import foundation.metaplex.rpc.RpcGetLatestBlockhashConfiguration
import foundation.metaplex.rpc.RpcSendTransactionConfiguration
import foundation.metaplex.solana.transactions.SolanaTransactionBuilder
import foundation.metaplex.solanapublickeys.PublicKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class WalletSigner(override val publicKey: PublicKey) : Signer {
  override suspend fun signMessage(message: ByteArray): ByteArray {
    TODO()
  }
}

class WelcomeViewModel : ViewModel() {
  private var signers: List<Signer> = emptyList()

  val connectedAccount = MutableStateFlow<PublicKey?>(null)
  val rpcConnection by inject<RPC>()
  val walletAdaptor by inject<WalletAdaptor>()

  fun connect() {
    walletAdaptor.connect { result ->
      result.onSuccess {
        val publicKey = PublicKey(it.walletPublicKey.encodeToBase58String())
        signers = listOf(WalletSigner(publicKey))
        connectedAccount.value = publicKey
      }
    }
  }

  fun flipCoin() {
    val account = connectedAccount.value ?: return

    viewModelScope.launch(Dispatchers.IO) {
      val recentBlockHash = runCatching {
        rpcConnection.getLatestBlockhash(RpcGetLatestBlockhashConfiguration(commitment = Commitment.finalized))
      }.onFailure {
        it.printStackTrace()
      }.getOrNull() ?: return@launch

      val solanaInstruction = SolanaTransactionBuilder()
        .addInstruction(CoinFlipProgram.methods.flip(account, CoinFlipProgram.args.FlipCoin(10.toULong())))
        .setRecentBlockHash(recentBlockHash.blockhash)
        .setSigners(signers)
        .build()

      val result = rpcConnection.sendTransaction(
        transaction = solanaInstruction.serialize(),
        configuration = RpcSendTransactionConfiguration(commitment = Commitment.confirmed)
      )

      println(result.encodeToBase58String())
    }
  }
}
