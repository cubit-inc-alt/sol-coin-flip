package core.ui

import androidx.compose.runtime.Immutable

/**
 * Represents a actionable item i.e. usable only once. Useful for one off things like toasts, snackbar etc
 * to use :
 *  1. Create a instance of SingleEvent(), usually in ViewModel
 *      var toastEvent  = SingleEvent<String?>()
 *  2. Observe the event
 *       LaunchedEffect(toastEvent) {
 *           toastEvent.actUpon{ message ->
 *              showToast(message)
 *           }
 *     }
 *  3. Fire event
 *     toastEvent  = SingleEvent("Toast Message")
 *  calling the [actUpOn] consumes the event and the callback passed on [actUpOn] is called once and only once per event
 */
@Immutable
class SingleEvent<T>(
     val value: T? = null,
) {
    private var consumed: Boolean = false

    /**
     * Useful for peeking into the underlying value without consuming the event itself
     */
    fun peek(): T? = value

    fun actUpOn(block: (T) -> Unit) {
        if (!consumed) {
            value?.let(block)
            consumed = true
        }
    }
}

fun <T> T.toEvent() = SingleEvent(this)
