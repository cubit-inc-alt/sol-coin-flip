package core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import core.designSystem.theme.AppDimensions.size_16
import core.designSystem.theme.AppDimensions.size_20
import core.designSystem.theme.AppTheme.typography
import core.resources.generated.resources.Res
import core.resources.generated.resources.ic_close
import org.jetbrains.compose.resources.painterResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseBottomSheet(
  title: String,
  isDismissible: Boolean = true,
  onDismissRequest: (Boolean) -> Unit = {},
  content: @Composable () -> Unit,
  footer: @Composable () -> Unit
) {
  ModalBottomSheet(
    onDismissRequest = {
      if (isDismissible) onDismissRequest(false)
    },
    sheetState = SheetState(skipPartiallyExpanded = false, density = LocalDensity.current),
    containerColor = Color.White, dragHandle = {}, shape = RoundedCornerShape(topStart = size_16, topEnd = size_16)

  ) {
    Column(
      verticalArrangement = Arrangement.Top,
      horizontalAlignment = Alignment.Start,
      modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(horizontal = size_16, vertical = size_20)
    ) {
      Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(title, style = typography.heading.LargeSemiBold)

        Image(
          painter = painterResource(Res.drawable.ic_close),
          contentDescription = "Close",
          modifier = Modifier
            .wrapContentSize()
            .clickable { onDismissRequest.invoke(false) },
          contentScale = ContentScale.Fit
        )
      }
      Column(
        Modifier.fillMaxWidth().wrapContentHeight().verticalScroll(rememberScrollState())
      ) {
        content()
      }
      footer()

    }
  }
}
