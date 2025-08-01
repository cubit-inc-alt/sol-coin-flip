package core.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.getOrNull
import com.github.kittinunf.result.runCatching
import core.common.inject
import core.data.repository.PlayRepository
import core.sol.NetworkCluster
import core.sol.WalletAdaptor
import core.sol.WalletConnectionStatus
import core.sol.WalletMethod.Connect
import core.sol.WalletMethod.SignTransaction
import core.ui.SingleEvent
import core.ui.components.toast.ToastState
import core.ui.components.toast.toastState
import core.ui.delegates.StateManager
import core.ui.delegates.ViewModelStateManager
import core.ui.delegates.setValue
import core.ui.navigation.AppNavigation
import core.ui.toEvent
import core.web3.coinFlip.CoinFlipProgram
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.KoinApplication.Companion.init


data class MainScreenState(
  val connectionStatus: WalletConnectionStatus,
  val achievement: Int? = null,
  val isWalletActive: Boolean = true,
  val wager: Float = 0.0f,
  val reset: Boolean = false,
  val nextDestination: SingleEvent<AppNavigation> = SingleEvent(),
  val isValid: Boolean = false,
) {
  val accountText
    get() = if (connectionStatus is WalletConnectionStatus.Connected) {
      val initial = connectionStatus.userAccountPublicKey.take(6)
      val end = connectionStatus.userAccountPublicKey.takeLast(4)

      "$initial..$end"
    } else "Not Connected"
}

class MainScreenViewModel(
  private val playRepository: PlayRepository,
  private val walletAdaptor: WalletAdaptor,
) : ViewModel(),
  ToastState by toastState(),
  StateManager<MainScreenState> by ViewModelStateManager(
    MainScreenState(connectionStatus = walletAdaptor.connectionStatus)
  ) {

  val rpcConnection by inject<RPC>()

  val playList = MutableStateFlow(playRepository.getPlayDataList())
  val setWager = updateField<Float> { copy(wager = it) }

  fun connect() = viewModelScope.launch(Dispatchers.IO) {
    val result = walletAdaptor.invoke(Connect(NetworkCluster.Devnet))

    when (result) {
      is Result.Failure -> println("error ${result.error}")
      is Result.Success -> flipCoin()
    }
  }


  fun flipCoin() {
    val account = (walletAdaptor.connectionStatus as? WalletConnectionStatus.Connected)
      ?.userAccountPublicKey?.let(::PublicKey) ?: return run { connect() }

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
