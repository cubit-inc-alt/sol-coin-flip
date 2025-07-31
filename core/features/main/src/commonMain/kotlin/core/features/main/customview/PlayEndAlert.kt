package core.features.main.customview

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import core.designSystem.elements.DefaultButton
import core.designSystem.theme.AppDimensions.size_10
import core.designSystem.theme.AppDimensions.size_24
import core.designSystem.theme.AppDimensions.size_40
import core.designSystem.theme.AppTheme.colors
import core.designSystem.theme.AppTheme.typography
import core.resources.generated.resources.Res
import core.resources.generated.resources.ic_win_streak
import core.resources.generated.resources.keep_going_reward
import core.resources.generated.resources.tap_to_continue
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PlayEndAlert(
  modifier: Modifier = Modifier,
  onDismiss: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,

    confirmButton = {

      DefaultButton(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        text = stringResource(Res.string.tap_to_continue)
      ) {
        onDismiss()
      }

    }, // We'll build our own confirm button inside content
    modifier = Modifier.clip(RoundedCornerShape(size_24)),
    text = {
      Column(
        modifier = Modifier
          .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Circle icon

        Image(
          painter = painterResource(Res.drawable.ic_win_streak),
          contentDescription = null,
          modifier = Modifier.fillMaxWidth().height(220.dp)
        )

        Text(
          text = "3x Win Streak!",
          style = typography.display.SmallBold,
          modifier = Modifier.padding(top = size_10),
          textAlign = TextAlign.Center
        )

        Text(
          text = stringResource(Res.string.keep_going_reward),
          color = colors.secondary,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(size_40))

      }
    },
    containerColor = Color.White,

  )
}
