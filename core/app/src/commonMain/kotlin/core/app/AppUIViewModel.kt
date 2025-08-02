package core.app

import androidx.lifecycle.ViewModel
import core.data.repository.AuthRepository
import core.ui.components.toast.ToastState
import core.ui.components.toast.toastState
import core.ui.delegates.StateManager
import core.ui.delegates.ViewModelStateManager
import core.datastore.DataStore
object AppState

class AppUIViewModel(
  datastore: DataStore
) : ViewModel(), StateManager<AppState> by ViewModelStateManager(
  AppState
), ToastState by toastState() {
  val termsAccepted = datastore.termsAccepted
}
