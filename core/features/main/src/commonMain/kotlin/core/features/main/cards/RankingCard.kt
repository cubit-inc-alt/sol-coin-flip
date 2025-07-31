package core.features.main.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import core.designSystem.theme.AppDimensions.size_10
import core.designSystem.theme.AppDimensions.size_12
import core.designSystem.theme.AppDimensions.size_16
import core.designSystem.theme.AppDimensions.size_20
import core.designSystem.theme.AppDimensions.size_30
import core.designSystem.theme.AppDimensions.size_32
import core.designSystem.theme.AppTheme.typography
import core.models.local.Play

@Composable
fun RankingCard(
  modifier: Modifier, index: Int, play: Play
) {
  Column(
    modifier.border(1.dp, color = Color.White, shape = RoundedCornerShape(size_32))
      .padding(vertical = size_20, horizontal = size_30), verticalArrangement = Arrangement.spacedBy(size_12)
  ) {
    Row(
      Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(size_16),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier.wrapContentSize().clip(CircleShape).background(
          brush = Brush.linearGradient(
            colors = listOf(
              Color(0xFFEAB308),
              Color(0xFFF3D190)
            )
          )
        ).padding(size_10)
      ) {
        Text("#$index", style = typography.body.SmallMedium, color = Color.White)
      }
      Text(
        play.id,
        style = typography.body.DefaultMedium,
        modifier = Modifier.weight(1f), overflow = TextOverflow.Ellipsis
      )
      Text(play.achievement, style = typography.body.DefaultBold)
    }
  }
}
