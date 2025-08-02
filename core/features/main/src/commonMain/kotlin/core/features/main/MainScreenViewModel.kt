package core.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.getOrNull
import com.github.kittinunf.result.runCatching
import core.common.inject
import core.data.repository.PlayRepository
import core.models.DoubleOrNothing
import core.network.webSocket.SocketConnection
import core.sol.NetworkCluster
import core.sol.WalletAdaptor
import core.sol.ConnectionStatus
import core.sol.WalletMethod.Connect
import core.sol.WalletMethod.SignTransaction
import core.sol.sol
import core.sol.solAmount
import core.ui.SingleEvent
import core.ui.components.toast.ToastState
import core.ui.components.toast.toastState
import core.ui.delegates.StateManager
import core.ui.delegates.ViewModelStateManager
import core.ui.delegates.setValue
import core.ui.delegates.stateOf
import core.ui.navigation.AppNavigation
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.getValue
import kotlin.math.pow
import kotlin.math.roundToInt

sealed class FlipStatus(val wager: ULong) {
  class WaitingUserConfirmation(
    wagerLamport: ULong,
    val signedTransaction: ByteArray
  ) : FlipStatus(wagerLamport)

  class SendingTransaction(wagerLamport: ULong) : FlipStatus(wagerLamport)
  class WaitingForChainConformation(wagerLamport: ULong, val tnxHash: String) : FlipStatus(wagerLamport)
  class FlipSuccess(
    wagerLamport: ULong,
    val tnxHash: String,
    val result: DoubleOrNothing
  ) : FlipStatus(wagerLamport)
}

data class MainScreenState(
  val connectionStatus: ConnectionStatus,
  val achievement: Int? = null,
  val wager: Long = 0,
  val reset: Boolean = false,
  val nextDestination: SingleEvent<AppNavigation> = SingleEvent(),
  val isValid: Boolean = false,
) {
  val accountText
    get() = if (connectionStatus is ConnectionStatus.Connected) {
      connectionStatus.userAccountPublicKey.timedHash
    } else "Not Connected"

  val formattedWager get() = "${wager.toDouble().div(100).formatAsCurrency()} SOL"

  val isWalletConnected get() = connectionStatus is ConnectionStatus.Connected
}

val FlipStatus.formattedWager get() = "${wager.solAmount().formatAsCurrency()} SOL"
val FlipStatus.formattedPossibleOutcome
  get() = "+${
    wager.times(2u).solAmount().formatAsCurrency()
  } SOL / -${formattedWager}"
val FlipStatus.FlipSuccess.formattedOutcome
  get() = when (result) {
    DoubleOrNothing.Double -> "+${wager.times(2u).solAmount().formatAsCurrency()} SOL"
    DoubleOrNothing.Nothing -> "-${formattedWager}"
  }
val FlipStatus.formattedOutcomePositive get() = "+${wager.times(2u).solAmount().formatAsCurrency()} SOL"
val FlipStatus.formattedOutcomeNegative get() = "-${formattedWager}"

class MainScreenViewModel(
  private val playRepository: PlayRepository,
  private val walletAdaptor: WalletAdaptor,
) : ViewModel(),
  ToastState by toastState(),
  StateManager<MainScreenState> by ViewModelStateManager(
    MainScreenState(connectionStatus = walletAdaptor.connectionStatus)
  ) {

  val rpcConnection by inject<RPC>()
  val socketConnection by inject<SocketConnection>()

  private var listenForTnxEvent: Job? = null

  val flipStatus = stateOf<FlipStatus?>(FlipStatus.WaitingForChainConformation(0.11.sol, "5S6oV8X2ycUZm4LdkrvZsRuqVYtWC2xCeQUo6Hx2YwrMWUzboL2RWZ6f7oUwX4pWxmBhgtsxQagoBCgL1EvNvRp4"))

  val playList = playRepository.plays
    .map { it.toList().sortedByDescending { play -> play.chainTimestamp } }
    .stateIn(viewModelScope + Dispatchers.IO, SharingStarted.Eagerly, emptyList())

  init {
    refreshPlays()

    viewModelScope.launch {
      delay(5000)
      flipStatus.setValue(FlipStatus.FlipSuccess(0.11.sol, "5S6oV8X2ycUZm4LdkrvZsRuqVYtWC2xCeQUo6Hx2YwrMWUzboL2RWZ6f7oUwX4pWxmBhgtsxQagoBCgL1EvNvRp4", DoubleOrNothing.Nothing))
    }
  }

  val setWager = updateField<Float> {
    copy(wager = it.times(100).toLong()).also {
      println("${it.wager.sol}, ${it.wager}  ${it.wager.toFloat() / 100f}")
    }

  }

  private fun refreshPlays() = viewModelScope.launch {
    playRepository.refreshPlays()
  }

  fun clearFlipStatus() = flipStatus.setValue(null)

  fun connect() = viewModelScope.launch(Dispatchers.IO) {
    val result = walletAdaptor.invoke(Connect(NetworkCluster.Devnet))

    when (result) {
      is Result.Failure -> println("error ${result.error}")
      is Result.Success -> {
        updateState {
          copy(connectionStatus = walletAdaptor.connectionStatus)
        }
      }
    }
  }

  fun flipCoin() {
    val account = (walletAdaptor.connectionStatus as? ConnectionStatus.Connected)
      ?.userAccountPublicKey?.let(::PublicKey) ?: return run { connect() }

    val wagerLamport = state.value.wager.takeIf { it > 0 }?.sol?.div(100u) ?: return

    viewModelScope.launch(Dispatchers.IO) {
      val recentTnx = runCatching {
        rpcConnection.getLatestBlockhash(RpcGetLatestBlockhashConfiguration(commitment = Commitment.finalized))
      }.getOrNull() ?: return@launch

      val transaction = SolanaTransaction().apply {
        feePayer = account
        recentBlockhash = recentTnx.blockhash
        addInstruction(CoinFlipProgram.methods.flip(account, CoinFlipProgram.args.FlipCoin(wagerLamport)))
      }

      val serialized: SerializedTransaction = transaction
        .serialize(SerializeConfig(requireAllSignatures = false, verifySignatures = false))

      when (val result = walletAdaptor.signTransaction(SignTransaction(serialized))) {
        is Result.Failure -> println("flipCoin:error ${result.error}")
        is Result.Success -> {
          flipStatus.setValue(FlipStatus.WaitingUserConfirmation(wagerLamport, result.value.transaction))
        }
      }
    }
  }

  private fun listenForTnxEvent(status: FlipStatus.WaitingForChainConformation) {
    listenForTnxEvent = viewModelScope.launch {
      playRepository.newPlayEvents
        .consumeAsFlow()
        .filter {
          it.tnxHash.lowercase() == status.tnxHash.lowercase()
        }
        .collect {
          flipStatus.setValue(FlipStatus.FlipSuccess(status.wager, status.tnxHash, it.result))
          listenForTnxEvent?.cancel()
        }
    }
    socketConnection.response
  }

  fun confirmTransaction(status: FlipStatus.WaitingUserConfirmation) = viewModelScope.launch(Dispatchers.IO) {

    flipStatus.setValue(FlipStatus.SendingTransaction(status.wager))

    val result = runCatching {
      rpcConnection.sendTransaction(
        status.signedTransaction,
        RpcSendTransactionConfiguration(commitment = Commitment.finalized)
      )
    }

    when (result) {
      is Result.Failure -> clearFlipStatus()
      is Result.Success -> {
        val status = FlipStatus.WaitingForChainConformation(
          wagerLamport = status.wager,
          tnxHash = result.value?.decodeToString().orEmpty()
        )
        flipStatus.setValue(status)
        listenForTnxEvent(status)
      }
    }
  }
}


fun Double.formatAsCurrency(): String {

  val fraction = ((this - toInt()) * 100)
    .toInt()
    .toString()
    .padStart(2, '0')

  return "${toInt().toString().padStart(2, '0')}.$fraction"
}

fun Float.fix(decimalPlaces: Int): Float {
  val multiplier = 10.0.pow(decimalPlaces)
  return (this * multiplier).roundToInt() / multiplier.toFloat()
}

val String.timedHash get() = "${take(6)}..${takeLast(4)}"


fun Instant.relativeTime(now: Instant = Clock.System.now()): String {
  val duration = now - this
  val seconds = duration.inWholeSeconds

  return when {
    seconds < 20 -> "Just now"
    seconds < 60 -> "$seconds second${if (seconds != 1L) "s" else ""} ago"
    duration.inWholeMinutes < 60 -> {
      val minutes = duration.inWholeMinutes
      "$minutes minute${if (minutes != 1L) "s" else ""} ago"
    }

    duration.inWholeHours < 24 -> {
      val hours = duration.inWholeHours
      "$hours hour${if (hours != 1L) "s" else ""} ago"
    }

    duration.inWholeDays < 30 -> {
      val days = duration.inWholeDays
      "$days day${if (days != 1L) "s" else ""} ago"
    }

    duration.inWholeDays < 365 -> {
      val months = duration.inWholeDays / 30
      "$months month${if (months != 1L) "s" else ""} ago"
    }

    else -> {
      val years = duration.inWholeDays / 365
      "$years year${if (years != 1L) "s" else ""} ago"
    }
  }
}
