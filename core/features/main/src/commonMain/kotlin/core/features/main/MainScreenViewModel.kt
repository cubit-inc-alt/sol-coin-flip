package core.features.main

import androidx.lifecycle.ViewModel
import core.data.repository.PlayRepository
import core.ui.SingleEvent
import core.ui.components.toast.ToastState
import core.ui.components.toast.toastState
import core.ui.delegates.StateManager
import core.ui.delegates.ViewModelStateManager
import core.ui.navigation.AppNavigation
import kotlinx.coroutines.flow.MutableStateFlow


data class MainScreenState(
  val id: String = "0xa1b2c34d4",
  val achievement: Int? = null,
  val isWalletActive: Boolean = true,
  val wager: Float? = 0.0f,
  val reset: Boolean = false,
  val nextDestination: SingleEvent<AppNavigation> = SingleEvent(),
  val isValid: Boolean = false,
)

class MainScreenViewModel(
  private val playRepository: PlayRepository
) : ViewModel(), StateManager<MainScreenState> by ViewModelStateManager(
  MainScreenState()
), ToastState by toastState() {

  val playList = MutableStateFlow(playRepository.getPlayDataList())
  val setWager = updateField<Float> { copy(wager = it) }


}
