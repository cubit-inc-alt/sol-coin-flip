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
import core.features.connectWallet.selectWallet.Wallet
import core.sol.NetworkCluster
import core.sol.WalletAdaptor
import core.sol.WalletMethod
import core.sol.WalletMethod.*
import core.ui.SingleEvent
import core.ui.delegates.setValue
import core.ui.delegates.stateOf
import core.ui.toEvent
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


class WelcomeViewModel : ViewModel() {
  val connectedAccount = MutableStateFlow<PublicKey?>(null)
  val walletAdaptor by inject<WalletAdaptor>()
  val onWalletConnected = stateOf(SingleEvent<Unit>(null))

  fun connect(
    wallet: Wallet
  ) = viewModelScope.launch(Dispatchers.IO) {
    val result = walletAdaptor.invoke(Connect(NetworkCluster.Devnet))

    when (result) {
      is Result.Failure -> println("error ${result.error}")
      is Result.Success -> {
        val publicKey = PublicKey(result.value.userWalletPublicKey)
        connectedAccount.value = publicKey
        onWalletConnected.setValue(Unit.toEvent())
      }
    }
  }
}


