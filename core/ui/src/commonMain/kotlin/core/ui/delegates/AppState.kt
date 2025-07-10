package core.ui.delegates

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.native.HiddenFromObjC

sealed interface AppState<T> : StateFlow<T>

internal class PandaStateImpl<T>(
    delegate: MutableStateFlow<T>,
) : MutableStateFlow<T> by delegate, AppState<T>

/**
 * Creates a new [AppState] which is a [MutableStateFlow] but restricts the update to [ViewModel] scope
 */
@HiddenFromObjC
fun <T> stateOf(initial: T): AppState<T> = PandaStateImpl(MutableStateFlow(initial))

@HiddenFromObjC
fun <T> AppState<T>.update(block: T.() -> T) {
    (this as PandaStateImpl<T>).value = block(value)
}

@HiddenFromObjC
fun <T> AppState<T>.setValue(value: T) {
    (this as PandaStateImpl<T>).value = value
}

fun <T> MutableStateFlow<T>.setValue(value: T) {
    this.value = value
}
