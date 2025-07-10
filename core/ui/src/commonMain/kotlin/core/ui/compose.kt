package core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun <T> rememberDerivedStateOf(
    calculation: @DisallowComposableCalls () -> T
): State<T> = remember {
    derivedStateOf(calculation)
}

@Composable
fun <T> rememberMutableStateOf(
    value: T
): MutableState<T> = remember {
    mutableStateOf(value)
}
