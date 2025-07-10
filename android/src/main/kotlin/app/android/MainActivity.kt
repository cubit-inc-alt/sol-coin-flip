package app.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import core.app.AppUI
import core.app.AppUIViewModel
import core.common.inject


class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val viewModel by inject<AppUIViewModel>()
      AppUI(viewModel)
    }
  }
}


