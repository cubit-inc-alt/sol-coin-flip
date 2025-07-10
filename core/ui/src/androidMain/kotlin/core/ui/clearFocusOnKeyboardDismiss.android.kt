package core.ui

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager

@OptIn(ExperimentalLayoutApi::class)
@Composable
actual fun Modifier.clearFocusOnKeyboardDismiss(): Modifier {
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }
    var keyboardAppearedSinceLastFocused by remember { mutableStateOf(false) }
    val imeVisible = WindowInsets.isImeVisible

    if (isFocused) {
        LaunchedEffect(imeVisible) {
            if (imeVisible) {
                keyboardAppearedSinceLastFocused = true
            } else if (keyboardAppearedSinceLastFocused) {
                focusManager.clearFocus()
            }
        }
    }

    return this
        .onFocusChanged {
            if (isFocused != it.isFocused) {
                isFocused = it.isFocused
                if (isFocused) keyboardAppearedSinceLastFocused = false
            }
        }
}
