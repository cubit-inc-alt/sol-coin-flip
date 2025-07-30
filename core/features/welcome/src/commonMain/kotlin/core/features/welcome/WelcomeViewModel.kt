package core.features.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.result.onFailure
import com.github.kittinunf.result.onSuccess
import com.metaplex.signer.Signer
import com.solana.transaction.Message
import core.common.inject
import core.sol.WalletAdaptor

import foundation.metaplex.rpc.Commitment
import foundation.metaplex.rpc.RPC
import foundation.metaplex.rpc.RpcGetLatestBlockhashConfiguration
import foundation.metaplex.solana.programs.SystemProgram
import foundation.metaplex.solana.transactions.SolanaTransaction
import foundation.metaplex.solanapublickeys.PublicKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine


class WalletSigner(
  override val publicKey: PublicKey,
  val walletAdaptor: WalletAdaptor
) : Signer {
  @OptIn(InternalCoroutinesApi::class)
  override suspend fun signMessage(message: ByteArray) = suspendCancellableCoroutine { continuation ->
    walletAdaptor.signTransaction(message) { result ->
      result
        .onSuccess { continuation.tryResume(it.content) }
        .onFailure { continuation.tryResumeWithException(it) }
    }
  }
}


class WelcomeViewModel : ViewModel() {
  private var signers: List<Signer> = emptyList()

  val connectedAccount = MutableStateFlow<PublicKey?>(null)
  val rpcConnection by inject<RPC>()
  val walletAdaptor by inject<WalletAdaptor>()

  fun connect() {
    walletAdaptor.connect("mainnet-beta") { result ->
      result.onSuccess {
        val publicKey = PublicKey(it.userWalletPublicKey)
        signers = listOf(WalletSigner(publicKey, walletAdaptor))
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

      val transaction = SolanaTransaction().apply {
        feePayer = signers.first().publicKey
        recentBlockhash = recentBlockHash.blockhash
        addInstruction(
          SystemProgram.transfer(
            fromPublicKey = feePayer!!,
            toPublickKey = PublicKey("EJB2o9rjoq6TEmTGNY2Uyb3oSiHut4scZgY5swRHwrVP"),
            lamports = 10
          )
        )
      }

      val signed = transaction.compileMessage()

      runCatching {
        walletAdaptor.signTransaction(signed.serialize()){
          println(it)
        }
      }
    }
  }
}
