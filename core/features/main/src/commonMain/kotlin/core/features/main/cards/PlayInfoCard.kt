package core.features.main.cards

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import core.designSystem.theme.AppColors
import core.designSystem.theme.AppDimensions.size_10
import core.designSystem.theme.AppDimensions.size_12
import core.designSystem.theme.AppDimensions.size_20
import core.designSystem.theme.AppDimensions.size_30
import core.designSystem.theme.AppDimensions.size_32
import core.designSystem.theme.AppTheme.colors
import core.designSystem.theme.AppTheme.typography
import core.features.main.formatAsCurrency
import core.features.main.relativeTime
import core.features.main.timedHash
import core.models.CoinFlip
import core.models.DoubleOrNothing
import core.models.tnxAsExplorerLink
import core.sol.solAmount


val CoinFlip.formattedAmount: String
  get() {
    val actualAmount = if (result == DoubleOrNothing.Double) amount.times(2u) else amount

    val formattedValue = if (actualAmount.solAmount() < 0.01) "<0.00 SOL"
    else "${actualAmount.solAmount().formatAsCurrency()} SOL"

    return when (result) {
      DoubleOrNothing.Double -> "+ $formattedValue"
      DoubleOrNothing.Nothing -> "- $formattedValue"
    }
  }


@Composable
fun PlayInfoCard(
  modifier: Modifier,
  play: CoinFlip
) {

  val linkHandler = LocalUriHandler.current

  Column(
    modifier.border(1.dp, color = Color.White, shape = RoundedCornerShape(size_32))
      .padding(vertical = size_20, horizontal = size_30),
    verticalArrangement = Arrangement.spacedBy(size_12)
  ) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(size_10)) {
      Text(
        text = play.tnxHash.timedHash,
        style = typography.body.DefaultMedium,
        color = colors.secondary,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        textDecoration = TextDecoration.Underline,
        modifier = Modifier.clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
          linkHandler.openUri(play.tnxHash.tnxAsExplorerLink(play.net))
        }
      )

      Spacer(modifier = Modifier.weight(1f))

      Text(play.chainTime.relativeTime(), style = typography.body.SmallRegular, color = colors.secondary)
    }

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(size_10)) {
      Text(
        text = play.result.name,
        style = typography.body.DefaultMedium,
        modifier = Modifier.weight(1f),
        overflow = TextOverflow.Ellipsis
      )

      Text(
        text = play.formattedAmount,
        style = typography.body.DefaultMedium,
        color = if (play.result == DoubleOrNothing.Double) AppColors.success else Color.Red
      )
    }
  }
}
