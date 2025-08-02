package core.sol

import com.ionspin.kotlin.bignum.decimal.BigDecimal

const val LAMPORT_IN_SOL: Long = 1_000_000_000

val Number.lamports: ULong
  get() = this.toDouble().toULong()

val Number.sol: ULong
  get() = BigDecimal.fromDouble(toDouble()).times(LAMPORT_IN_SOL)
    .toBigInteger()
    .ulongValue(false)

fun ULong.solAmount(): Double {
  return BigDecimal.fromULong(this).div(LAMPORT_IN_SOL)
    .doubleValue(false)
}
