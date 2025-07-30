package core.features.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.getOrNull
import com.github.kittinunf.result.onFailure
import com.github.kittinunf.result.onSuccess
import com.github.kittinunf.result.runCatching
import com.metaplex.signer.Signer
import core.common.inject
import core.sol.NetworkCluster
import core.sol.WalletAdaptor
import core.sol.WalletMethod
import core.sol.WalletMethod.*
import core.web3.coinFlip.CoinFlipProgram
import foundation.metaplex.base58.encodeToBase58String
import foundation.metaplex.rpc.Commitment

import foundation.metaplex.rpc.RPC
import foundation.metaplex.rpc.RpcGetLatestBlockhashConfiguration
import foundation.metaplex.rpc.RpcSendTransactionConfiguration
import foundation.metaplex.rpc.SerializedTransaction
import foundation.metaplex.solana.transactions.SerializeConfig
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
    walletAdaptor.signMessage(WalletMethod.SignMessage(message)) { result ->
      result
        .onSuccess { continuation.tryResume(it.signature) }
        .onFailure { continuation.tryResumeWithException(it) }
    }
  }
}


class WelcomeViewModel : ViewModel() {
  val connectedAccount = MutableStateFlow<PublicKey?>(null)
  val rpcConnection by inject<RPC>()
  val walletAdaptor by inject<WalletAdaptor>()

  fun connect() = viewModelScope.launch(Dispatchers.IO) {
    val result = walletAdaptor.invoke(WalletMethod.Connect(NetworkCluster.Devnet))

    when (result) {
      is Result.Failure -> println("error ${result.error}")
      is Result.Success -> {
        val publicKey = PublicKey(result.value.userWalletPublicKey)
        connectedAccount.value = publicKey
      }
    }
  }

  fun flipCoin() {
    val account = connectedAccount.value ?: return

    viewModelScope.launch(Dispatchers.IO) {

      val recentTnx = runCatching {
        rpcConnection.getLatestBlockhash(RpcGetLatestBlockhashConfiguration(commitment = Commitment.finalized))
      }.getOrNull() ?: return@launch

      val transaction = SolanaTransaction().apply {
        feePayer = account
        recentBlockhash = recentTnx.blockhash
        addInstruction(CoinFlipProgram.methods.flip(account, CoinFlipProgram.args.FlipCoin(10.toULong())))
      }

      val serialized: SerializedTransaction = transaction
        .serialize(SerializeConfig(requireAllSignatures = false, verifySignatures = false))

      when (val result = walletAdaptor.signTransaction(SignTransaction(serialized))) {
        is Result.Failure -> println("flipCoin:error ${result.error}")
        is Result.Success -> sendTransaction(result.value.transaction)
      }
    }
  }
  private fun sendTransaction(tnx: ByteArray) = viewModelScope.launch(Dispatchers.IO) {

    println("sending tnx")

    val result = runCatching {
      rpcConnection.sendTransaction(tnx, RpcSendTransactionConfiguration(commitment = Commitment.finalized))
    }

    when (result) {
      is Result.Failure -> println("sendTransaction:error ${result.error}")
      is Result.Success -> {
        println("tnx sent ${result.value?.decodeToString()}")
      }
    }
  }
}


