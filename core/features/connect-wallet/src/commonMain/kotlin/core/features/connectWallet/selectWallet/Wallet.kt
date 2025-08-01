package core.features.connectWallet.selectWallet

import core.resources.generated.resources.Res
import core.resources.generated.resources.ic_meta_mask
import core.resources.generated.resources.ic_phantom
import core.resources.generated.resources.ic_trust_wallet
import core.resources.generated.resources.img_wallet
import core.resources.generated.resources.meta_mask
import core.resources.generated.resources.phantom_wallet
import core.resources.generated.resources.trust_wallet
import core.resources.generated.resources.wallet_connect
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class Wallet(
  val image: DrawableResource,
  val title: StringResource,
  val available: Boolean = false
) {
  MetaMask(Res.drawable.ic_meta_mask, Res.string.meta_mask),
  WalletConnect(Res.drawable.img_wallet, Res.string.wallet_connect),
  TrustWallet(Res.drawable.ic_trust_wallet, Res.string.trust_wallet),

  Phantom(Res.drawable.ic_phantom, Res.string.phantom_wallet, true)
}
