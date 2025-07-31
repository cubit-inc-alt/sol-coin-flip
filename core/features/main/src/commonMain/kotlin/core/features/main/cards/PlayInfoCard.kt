package core.features.main.cards

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import core.designSystem.theme.AppColors
import core.designSystem.theme.AppDimensions.size_10
import core.designSystem.theme.AppDimensions.size_12
import core.designSystem.theme.AppDimensions.size_20
import core.designSystem.theme.AppDimensions.size_30
import core.designSystem.theme.AppDimensions.size_32
import core.designSystem.theme.AppDimensions.size_40
import core.designSystem.theme.AppTheme.colors
import core.designSystem.theme.AppTheme.typography
import core.models.local.Play

@Composable
fun PlayInfoCard(
  modifier: Modifier, play: Play
) {
  Column(
    modifier.border(1.dp, color = Color.White, shape = RoundedCornerShape(size_32))
      .padding(vertical = size_20, horizontal = size_30), verticalArrangement = Arrangement.spacedBy(size_12)
  ) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(size_10)) {
      Text(
        play.id,
        style = typography.body.DefaultMedium,
        color = colors.secondary,
        modifier = Modifier.weight(1f), overflow = TextOverflow.Ellipsis
      )
      Text(play.dateTime, style = typography.body.SmallRegular, color = colors.secondary)
    }

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(size_10)) {
      Text(play.desc, style = typography.body.DefaultMedium,
        modifier = Modifier.weight(1f), overflow = TextOverflow.Ellipsis
      )
      Text(
        play.wager.toString() + " SOL",
        style = typography.body.DefaultMedium,
        color = if (play.wager <= 0) Color.Red else AppColors.success
      )
    }

  }
}
