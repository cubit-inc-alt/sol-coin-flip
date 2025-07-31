package core.features.main.customview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import core.designSystem.elements.DefaultButton
import core.designSystem.elements.DefaultOutlineButton
import core.designSystem.theme.AppDimensions.size_12
import core.designSystem.theme.AppDimensions.size_16
import core.designSystem.theme.AppDimensions.size_4
import core.designSystem.theme.AppDimensions.size_8
import core.designSystem.theme.AppTheme.colors
import core.designSystem.theme.AppTheme.shapes
import core.designSystem.theme.AppTheme.typography
import core.resources.generated.resources.Res
import core.resources.generated.resources.amount
import core.resources.generated.resources.cancel
import core.resources.generated.resources.confirm
import core.resources.generated.resources.confirm_wager
import core.resources.generated.resources.ic_info
import core.resources.generated.resources.possible_outcome
import core.resources.generated.resources.real_transaction_warning
import core.resources.generated.resources.token
import core.ui.components.BaseBottomSheet
import core.ui.rememberMutableStateOf
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ConfirmWagerBottomSheet(
  amount: String,
  token: String,
  outcomes: String,
  onConfirmWager: (Float) -> Unit,
  isDismissible: Boolean = true,
  onDismissRequest: (Boolean) -> Unit = {},
) {
  var selectedWager by rememberMutableStateOf<Float>(0.0f)

  BaseBottomSheet(
    title = stringResource(
      Res.string.confirm_wager
    ),
    onDismissRequest = onDismissRequest,
    content = {
      Text(
        buildAnnotatedString {
          append(stringResource(Res.string.amount))
          append(" ")
          withStyle(
            SpanStyle(
              fontWeight = FontWeight.SemiBold
            )
          ) {
            append(amount)
          }
        },
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = size_12),
        style = typography.body.DefaultRegular
      )
      Text(
        buildAnnotatedString {
          append(stringResource(Res.string.token))
          append(" ")
          withStyle(
            SpanStyle(
              fontWeight = FontWeight.SemiBold
            )
          ) {
            append(token)
          }
        },
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = size_4),
        style = typography.body.DefaultRegular
      )
      Text(
        buildAnnotatedString {
          append(stringResource(Res.string.possible_outcome))
          append(" ")
          withStyle(
            SpanStyle(
              fontWeight = FontWeight.SemiBold
            )
          ) {
            append(outcomes)
          }
        },
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = size_4),
        style = typography.body.DefaultRegular
      )
      Row(
        Modifier.padding(top = size_12).fillMaxWidth().clip(shapes.medium).background(colors.secondaryContainer)
          .padding(size_12),
        horizontalArrangement = Arrangement.spacedBy(size_8), verticalAlignment = Alignment.CenterVertically
      ) {

        Image(
          painter = painterResource(Res.drawable.ic_info),
          contentDescription = "",
          colorFilter = ColorFilter.tint(Color.Black),
          contentScale = ContentScale.Fit,
          modifier = Modifier.padding(end = size_8).size(size_16)
        )
        Text(
          stringResource(Res.string.real_transaction_warning),
          style = typography.body.DefaultRegular,
        )
      }

    }) {
    Row(horizontalArrangement = Arrangement.spacedBy(size_12)) {
      DefaultOutlineButton(
        modifier = Modifier.weight(1f).wrapContentHeight().padding(top = size_12),
        text = stringResource(Res.string.cancel),
        onClick = {
          onDismissRequest(false)
        }
      )
      DefaultButton(
        modifier = Modifier.weight(1f).wrapContentHeight().padding(top = size_12),
        text = stringResource(Res.string.confirm),
        onClick = {
          onConfirmWager(selectedWager)
        }
      )
    }
  }
}
