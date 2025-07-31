package core.features.connect_wallet.selectWallet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import core.designSystem.elements.DefaultButton
import core.designSystem.theme.AppDimensions.size_12
import core.designSystem.theme.AppDimensions.size_24
import core.designSystem.theme.AppDimensions.size_8
import core.designSystem.theme.AppTheme.colors
import core.resources.generated.resources.Res
import core.resources.generated.resources.connect_wallet
import core.resources.generated.resources.connect_your_wallet
import core.resources.generated.resources.wallet_connect_desc
import core.ui.components.BaseBottomSheet
import core.ui.rememberMutableStateOf
import org.jetbrains.compose.resources.stringResource

@Composable
fun SelectWalletBottomSheet(
  onSelectWallet: (Wallet) -> Unit,
  isDismissible: Boolean = true,
  onDismissRequest: (Boolean) -> Unit = {},
) {
  var selectedWallet by rememberMutableStateOf<Wallet?>(null)

  BaseBottomSheet(
    title = stringResource(
      Res.string.connect_wallet
    ),
    onDismissRequest = onDismissRequest,
    content = {
      Text(
        stringResource(Res.string.wallet_connect_desc),
        modifier = Modifier.padding(top = size_12),
        color = colors.secondary
      )
      Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = size_24),
        horizontalArrangement = Arrangement.spacedBy(size_8)
      ) {
        WalletCard(
          modifier = Modifier
            .wrapContentHeight().weight(1f),
          wallet = Wallet.MetaMask, selectedWallet = selectedWallet, onSelectWallet = { selectedWallet = Wallet.MetaMask }

        )
        WalletCard(
          modifier = Modifier
            .wrapContentHeight().weight(1f),
          wallet = Wallet.WalletConnect, selectedWallet = selectedWallet, onSelectWallet = { selectedWallet = Wallet.WalletConnect }
        )
        WalletCard(
          modifier = Modifier
            .wrapContentHeight().weight(1f),
          wallet = Wallet.TrustWallet, selectedWallet = selectedWallet, onSelectWallet = { selectedWallet = Wallet.TrustWallet }

        )
      }

    }) {
    DefaultButton(
      modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(top = size_12),
      text = stringResource(Res.string.connect_your_wallet),
      enabled = selectedWallet != null,
      onClick = {
        selectedWallet?.let { onSelectWallet(it) }
      }
    )
  }
}
