package core.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface AppNavigation {

  @Serializable
  data object WelcomeScreen : AppNavigation

  @Serializable
  data object MainScreen : AppNavigation

  @Serializable
  data object WeeklyRankingScreen : AppNavigation

  @Serializable
  data object PopBack : AppNavigation


}
