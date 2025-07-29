@file:Suppress("ClassName")

package core.web3.coinFlip

import com.funkatronics.kborsh.Borsh
import foundation.metaplex.rpc.serializers.AnchorInstructionSerializer
import foundation.metaplex.solana.programs.SystemProgram
import foundation.metaplex.solana.transactions.AccountMeta
import foundation.metaplex.solana.transactions.TransactionInstruction
import foundation.metaplex.solanapublickeys.PublicKey
import kotlinx.serialization.Serializable

object CoinFlipProgram {
  val ID = PublicKey.valueOf("E6sfySsqcxrh7fFzGQoFhFTTjWduA3PzB5cgCoCxsQps")
  val vault = PublicKey.valueOf("EJB2o9rjoq6TEmTGNY2Uyb3oSiHut4scZgY5swRHwrVP")

  object methods {
    fun flip(
      payer: PublicKey,
      args: args.FlipCoin
    ): TransactionInstruction {
      val keys = mutableListOf<AccountMeta>()
      keys.add(AccountMeta(payer, isSigner = true, isWritable = true))
      keys.add(AccountMeta(vault, isSigner = false, isWritable = true))
      keys.add(AccountMeta(SystemProgram.PROGRAM_ID, isSigner = false, isWritable = false))

      return TransactionInstruction(
        programId = ID,
        keys = keys,
        data = Borsh.encodeToByteArray(
          serializer = AnchorInstructionSerializer("flip"),
          value = args,
        )
      )
    }
  }

  object args {
    @Serializable
    data class FlipCoin(val amount: ULong)
  }

  object types {
    @Serializable
    enum class DoubleOrNothing {
      Double,
      Nothing
    }
  }
}
