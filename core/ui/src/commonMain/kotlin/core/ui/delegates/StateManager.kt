package core.ui.delegates

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface StateManager<S> {

    val state: StateFlow<S>

    fun setState(newState: S)

    fun updateState(modifier: S.() -> S)

    fun <T> updateField(updater: S.(T) -> S): (T) -> Unit

}

class ViewModelStateManager<S>(initialState: S) : StateManager<S> {

    private val _state = MutableStateFlow(initialState)
    override val state: StateFlow<S> = _state.asStateFlow()

    override fun updateState(modifier: S.() -> S) {
        _state.update(modifier)
    }

    override fun setState(newState: S) {
        _state.update {
            newState
        }
    }

    override fun <T> updateField(updater: S.(T) -> S): (T) -> Unit {
        val a: (T) -> Unit = { value ->
            updateState {
                this.updater(value)
            }
        }
        return a
    }
}


