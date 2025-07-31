package core.designSystem.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import core.designSystem.theme.AppDimensions.size_54
import core.designSystem.theme.AppDimensions.size_8
import core.designSystem.theme.AppTheme.typography
import core.designSystem.theme.Shapes
import core.resources.generated.resources.Res
import core.resources.generated.resources.background_button
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

val BUTTON_SHAPE = RoundedCornerShape(27.dp)
val BUTTON_HEIGHT = size_54

@Composable
fun DefaultButtonText(
  label: String, textStyle: TextStyle = typography.body.DefaultBold, color: Color = LocalContentColor.current
) {
  Text(
    text = label,
    style = textStyle,
    color = color,
    modifier = Modifier.padding(vertical = size_8)
  )
}


@Composable
fun DefaultButton(
  modifier: Modifier,
  text: String,
  enabled: Boolean = true,
  startIcon: DrawableResource? = null,
  endIcon: DrawableResource? = null,
  colors: ButtonColors = ButtonDefaults.buttonColors(),
  padding: PaddingValues = ButtonDefaults.ContentPadding,
  contentColor: Color = Color.White,
  backgroundImage: DrawableResource = Res.drawable.background_button,
  onClick: () -> Unit
) {
  Box(
    modifier = modifier.height(BUTTON_HEIGHT)
      .clickable(onClick = onClick).clip(BUTTON_SHAPE), contentAlignment = Alignment.Center
  ) {
    Image(
      painter = painterResource(backgroundImage),
      contentDescription = null,
      contentScale = ContentScale.FillBounds,
      modifier = Modifier.matchParentSize(),
      colorFilter = if (!enabled) ColorFilter.tint(colors.disabledContentColor) else null
    )

    Box(
      modifier = Modifier
        .wrapContentSize()
        .padding(padding),
      contentAlignment = Alignment.Center
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(size_8)
      ) {
        if (startIcon != null) {
          Image(
            painter = painterResource(startIcon),
            contentDescription = "startIcon",
            colorFilter = ColorFilter.tint(contentColor)
          )
        }
        DefaultButtonText(label = text, color = contentColor)
        if (endIcon != null) {
          Image(
            painter = painterResource(endIcon),
            contentDescription = "endIcon",
            colorFilter = ColorFilter.tint(contentColor)
          )
        }
      }
    }
  }
}


@Composable
fun DefaultOutlineButton(
  modifier: Modifier = Modifier,
  text: String,
  enabled: Boolean = true,
  startIcon: DrawableResource? = null,
  endIcon: DrawableResource? = null,
  buttonColors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
  onClick: () -> Unit
) {
  val contentColor = if (enabled) buttonColors.contentColor else buttonColors.disabledContentColor

  OutlinedButton(
    onClick = onClick,
    enabled = enabled,
    modifier = modifier,
    shape = BUTTON_SHAPE,
    colors = buttonColors,
    border = BorderStroke(1.dp, contentColor)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(size_8)
    ) {
      if (startIcon != null) {
        Image(
          painter = painterResource(startIcon),
          contentDescription = "startIcon",
          colorFilter = ColorFilter.tint(contentColor)
        )
      }
      DefaultButtonText(label = text)
      if (endIcon != null) {
        Image(
          painter = painterResource(endIcon),
          contentDescription = "endIcon",
          colorFilter = ColorFilter.tint(contentColor)
        )
      }
    }
  }
}


@Composable
fun DefaultTextButton(
  modifier: Modifier = Modifier,
  text: String,
  enabled: Boolean = true,
  startIcon: DrawableResource? = null,
  endIcon: DrawableResource? = null,
  colors: ButtonColors = ButtonDefaults.textButtonColors(),
  onClick: () -> Unit
) {

  TextButton(
    onClick = onClick,
    enabled = enabled,
    modifier = modifier,
    shape = Shapes.buttonPrimary,
    colors = colors
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(size_8)
    ) {
      if (startIcon != null) {
        Image(
          painter = painterResource(startIcon),
          contentDescription = "startIcon",
          colorFilter = ColorFilter.tint(LocalContentColor.current)
        )
      }
      DefaultButtonText(label = text)
      if (endIcon != null) {
        Image(
          painter = painterResource(endIcon),
          contentDescription = "endIcon",
          colorFilter = ColorFilter.tint(LocalContentColor.current)
        )
      }
    }
  }
}

@Composable
fun DefaultTonalButton(
  modifier: Modifier = Modifier,
  text: String?,
  enabled: Boolean = true,
  startIcon: DrawableResource? = null,
  endIcon: DrawableResource? = null,
  colors: ButtonColors = ButtonDefaults.filledTonalButtonColors(),
  endIconTint: Color? = null,
  padding: PaddingValues = ButtonDefaults.ContentPadding,
  onClick: () -> Unit
) {

  FilledTonalButton(
    onClick = onClick,
    enabled = enabled,
    modifier = modifier.height(BUTTON_HEIGHT),
    shape = BUTTON_SHAPE,
    colors = colors, contentPadding = padding
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(size_8)
    ) {
      if (startIcon != null) {
        Image(
          painter = painterResource(startIcon),
          contentDescription = "startIcon",
          colorFilter = ColorFilter.tint(LocalContentColor.current)
        )
      }
      text?.let { DefaultButtonText(label = it) }
      if (endIcon != null) {
        Image(
          painter = painterResource(endIcon),
          contentDescription = "endIcon",
          colorFilter = ColorFilter.tint(endIconTint ?: LocalContentColor.current)
        )
      }
    }
  }
}

@Composable
fun NoRippleButton(
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
  content: @Composable () -> Unit
) {
  Box(
    modifier = modifier.clickable(
      indication = null,
      interactionSource = remember { MutableInteractionSource() }
    ) {
      onClick()
    },
    contentAlignment = Alignment.Center
  ) {
    content()
  }
}
