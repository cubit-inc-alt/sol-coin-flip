package core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import core.designSystem.theme.AppDimensions
import core.designSystem.theme.AppDimensions.size_20
import core.designSystem.theme.AppDimensions.size_24
import core.designSystem.theme.AppDimensions.size_36
import core.designSystem.theme.AppTheme.colors
import core.resources.generated.resources.Res
import core.resources.generated.resources.background_button
import org.jetbrains.compose.resources.painterResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSlider(position: Float, onValueChange: (Float) -> Unit) {
  var sliderPosition by rememberSaveable { mutableStateOf(0f) }
  val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
  LaunchedEffect(position) {
    sliderPosition = position
  }
  Slider(
    value = sliderPosition,
    onValueChange = {
      sliderPosition = it
      onValueChange(sliderPosition)
    },
    valueRange = 0f..10f,
    interactionSource = interactionSource,
    onValueChangeFinished = {},
    track = { sliderState ->
      Box(
        modifier = Modifier
          .fillMaxWidth().clip(RoundedCornerShape(size_24))
          .height(size_20)
      ) {
        Image(
          painter = painterResource(Res.drawable.background_button),
          contentDescription = "",
          modifier = Modifier
            .fillMaxWidth()
            .height(size_20),
          contentScale = ContentScale.Crop
        )
        SliderDefaults.Track(
          modifier = Modifier
            .fillMaxWidth()
            .height(size_20),
          sliderState = sliderState,
          thumbTrackGapSize = 0.dp,
          drawStopIndicator = null,
          colors = SliderDefaults.colors(activeTrackColor = Color.Transparent)
        )
      }
    },
    thumb = {
      SliderDefaults.Thumb(
        interactionSource = interactionSource,
        modifier = Modifier
          .size(size_36)
          .clip(CircleShape).background(colors.primary).border(4.dp, colors.primary.copy(0.4f), CircleShape)
          .border(4.dp, Color.White, CircleShape),
        colors = SliderDefaults.colors(thumbColor = Color.Transparent)
      )
    },
    modifier = Modifier.padding(top = AppDimensions.size_8)
  )

}
