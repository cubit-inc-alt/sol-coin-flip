package core.features.connect_wallet.selectWallet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import core.designSystem.theme.AppDimensions.size_12
import core.designSystem.theme.AppDimensions.size_16
import core.designSystem.theme.AppDimensions.size_24
import core.designSystem.theme.AppDimensions.size_40
import core.designSystem.theme.AppDimensions.size_8
import core.designSystem.theme.AppTheme.colors
import core.designSystem.theme.AppTheme.typography
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun WalletCard(
    modifier: Modifier,
    wallet: Wallet,
    selectedWallet: Wallet?,
    onSelectWallet: (Wallet) -> Unit,
) {
    val selectedModifier = Modifier
        .clip(RoundedCornerShape(size_16))
        .background(colors.surface)
        .border(
            width = 1.dp,
            color = Color.Black,
            shape = RoundedCornerShape(size_24)
        )
        .clipToBounds()

    val finalModifier = if (selectedWallet == wallet) modifier.then(selectedModifier) else modifier

    Column(
        finalModifier.clickable {
            onSelectWallet(wallet)
        }.padding(vertical = size_12),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(size_8)
    ) {
        Image(
            painter = painterResource(wallet.image),
            contentDescription = "",
            modifier = Modifier.size(size_40)
        )
        Text(text = stringResource(wallet.title), style = typography.body.DefaultSemiBold)
    }
}
