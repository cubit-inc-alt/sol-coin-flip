package core.designSystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.interop.LocalUIViewController
import platform.UIKit.UIColor
import platform.UIKit.UIUserInterfaceStyle


private val Color.uiColor: UIColor
    get() = UIColor(
        red = this.red.toDouble(),
        green = this.green.toDouble(),
        blue = this.blue.toDouble(),
        alpha = this.alpha.toDouble(),
    )

fun Color.toUIColor(): UIColor = UIColor(
    red = this.red.toDouble(),
    green = this.green.toDouble(),
    blue = this.blue.toDouble(),
    alpha = this.alpha.toDouble(),
)

@Composable
internal actual fun SyncOsTheme(userThemeMode: UserThemeMode, themeMode: ThemeMode) {
    val uiController = LocalUIViewController.current
    val backgroundColor = AppTheme.colors.background

    LaunchedEffect(backgroundColor, userThemeMode, themeMode) {
        val window = uiController.view.window ?: return@LaunchedEffect

        window.backgroundColor = backgroundColor.toUIColor()

        window.overrideUserInterfaceStyle = when (userThemeMode) {
            UserThemeMode.System -> UIUserInterfaceStyle.UIUserInterfaceStyleUnspecified
            UserThemeMode.Light -> UIUserInterfaceStyle.UIUserInterfaceStyleLight
            UserThemeMode.Dark -> UIUserInterfaceStyle.UIUserInterfaceStyleDark
            UserThemeMode.Black -> UIUserInterfaceStyle.UIUserInterfaceStyleDark
        }
    }
}
