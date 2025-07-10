package core.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface AppNavigation {

  @Serializable
  data object WelcomeScreen : AppNavigation

}
