package core.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import core.designSystem.theme.AppTheme


@Composable
fun PreviewScreen() {
    Column(Modifier.padding(20.dp)) {

        Text("Colors")
        ColorView()
        Spacer(Modifier.size(16.dp))

    }
}


data class ThemeColorItem(val name: String, val color: Color)

@Composable
fun ColorView() {
    val colors = AppTheme.colors
    val colorList = listOf(
        ThemeColorItem("primary", colors.primary),
        ThemeColorItem("onPrimary", colors.onPrimary),
        ThemeColorItem("primaryContainer", colors.primaryContainer),
        ThemeColorItem("onPrimaryContainer", colors.onPrimaryContainer),
        ThemeColorItem("inversePrimary", colors.inversePrimary),
        ThemeColorItem("secondary", colors.secondary),
        ThemeColorItem("onSecondary", colors.onSecondary),
        ThemeColorItem("secondaryContainer", colors.secondaryContainer),
        ThemeColorItem("onSecondaryContainer", colors.onSecondaryContainer),
        ThemeColorItem("tertiary", colors.tertiary),
        ThemeColorItem("onTertiary", colors.onTertiary),
        ThemeColorItem("tertiaryContainer", colors.tertiaryContainer),
        ThemeColorItem("onTertiaryContainer", colors.onTertiaryContainer),
        ThemeColorItem("background", colors.background),
        ThemeColorItem("onBackground", colors.onBackground),
        ThemeColorItem("surface", colors.surface),
        ThemeColorItem("onSurface", colors.onSurface),
        ThemeColorItem("surfaceVariant", colors.surfaceVariant),
        ThemeColorItem("onSurfaceVariant", colors.onSurfaceVariant),
        ThemeColorItem("surfaceTint", colors.surfaceTint),
        ThemeColorItem("inverseSurface", colors.inverseSurface),
        ThemeColorItem("inverseOnSurface", colors.inverseOnSurface),
        ThemeColorItem("error", colors.error),
        ThemeColorItem("onError", colors.onError),
        ThemeColorItem("errorContainer", colors.errorContainer),
        ThemeColorItem("onErrorContainer", colors.onErrorContainer),
        ThemeColorItem("outline", colors.outline),
        ThemeColorItem("outlineVariant", colors.outlineVariant),
        ThemeColorItem("scrim", colors.scrim),
        ThemeColorItem("surfaceBright", colors.surfaceBright),
        ThemeColorItem("surfaceDim", colors.surfaceDim),
        ThemeColorItem("surfaceContainer", colors.surfaceContainer),
        ThemeColorItem("surfaceContainerHigh", colors.surfaceContainerHigh),
        ThemeColorItem("surfaceContainerHighest", colors.surfaceContainerHighest),
        ThemeColorItem("surfaceContainerLow", colors.surfaceContainerLow),
        ThemeColorItem("surfaceContainerLowest", colors.surfaceContainerLowest),
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(colorList) { item ->
            ColorRow(item)
        }
    }

}


@Composable
private fun ColorRow(item: ThemeColorItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(item.color)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = item.name,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

