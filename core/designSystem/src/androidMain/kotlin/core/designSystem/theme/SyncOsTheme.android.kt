package core.designSystem.theme

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.WindowCompat

@Composable
internal actual fun SyncOsTheme(userThemeMode: UserThemeMode, themeMode: ThemeMode) {
  val activity = LocalContext.current as? Activity
  val view = LocalView.current
  val colors = AppTheme.colors

  activity?.let {
    LaunchedEffect(themeMode) {
      WindowCompat.getInsetsController(activity.window, view).apply {
        isAppearanceLightStatusBars = !themeMode.isDark()
        isAppearanceLightNavigationBars = !themeMode.isDark()
        activity.window.setBackgroundDrawable(colors.background.toArgb().toDrawable())
      }
    }
  }
}


