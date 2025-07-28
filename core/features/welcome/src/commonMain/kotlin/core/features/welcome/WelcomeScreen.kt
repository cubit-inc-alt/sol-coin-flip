package core.features.welcome

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import core.common.AppConfig
import core.common.isDebugBuild

@Composable
fun WelcomeScreen() {
  Scaffold {
    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center
    ) {
      Text(
        "Welcome Environment: ${AppConfig.environment}, isDebugBuild:${isDebugBuild()}",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyLarge
      )
    }
  }
}
