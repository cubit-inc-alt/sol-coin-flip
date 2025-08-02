package core.models

import androidx.compose.ui.input.key.Key.Companion.T
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

enum class DoubleOrNothing {
  Double,
  Nothing
}


enum class SolNetwork {
  DevNet,
  MainNet;
}


@Serializable
class CoinFlip(
  val tnxHash: String,
  val programId: String,
  val account: String,
  val result: DoubleOrNothing,
  val amount: ULong,
  val slot: Long,
  val net: SolNetwork,
  val chainTimestamp: Long,
  val createdAt: Instant
) {
  override fun hashCode(): Int {
    return tnxHash.hashCode()
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || this::class != other::class) return false

    return this.tnxHash == (other as CoinFlip).tnxHash
  }

  val chainTime = Instant.fromEpochSeconds(chainTimestamp)

}


fun String.tnxAsExplorerLink(net: SolNetwork) = "https://explorer.solana.com/tx/$this".let {
  if (net == SolNetwork.DevNet) "$it?cluster=devnet"
  else it
}
