package core.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import core.designSystem.theme.AppTheme
import core.features.WeeklyRanking.ranking.WeeklyRankingScreen
import core.features.main.MainScreen
import core.features.welcome.WelcomeScreen
import core.sol.WalletAdaptor
import core.ui.components.toast.ToastHost
import core.ui.navigation.AppNavigation
import org.koin.compose.koinInject


@OptIn(ExperimentalCoilApi::class)
@Composable
fun AppUI(viewModel: AppUIViewModel) {

  val walletAdaptor = koinInject<WalletAdaptor>()

  setSingletonImageLoaderFactory { context ->
    ImageLoader.Builder(context).crossfade(true).logger(DebugLogger()).build()
  }

  val urlHandler = LocalUriHandler.current

  LaunchedEffect(urlHandler) {
    walletAdaptor.urlHandler = urlHandler
  }

  AppTheme {
    val navigator = rememberNavController()
    Box(Modifier.fillMaxSize()) {
      ToastHost {
        NavHost(
          navController = navigator,
          startDestination = if (viewModel.termsAccepted) AppNavigation.MainScreen else AppNavigation.WelcomeScreen
        ) {
          composable<AppNavigation.WelcomeScreen> {
            WelcomeScreen {
              navigator.navigate(AppNavigation.MainScreen) {
                popUpTo(AppNavigation.WelcomeScreen) {
                  inclusive = true
                }
                launchSingleTop = true
              }
            }
          }

          composable<AppNavigation.MainScreen> {
            MainScreen(koinInject())

            composable<AppNavigation.WeeklyRankingScreen> {
              WeeklyRankingScreen(koinInject()) {
                when (it) {
                  AppNavigation.PopBack -> navigator.popBackStack()
                  else -> navigator.navigate(it) {
                    popUpTo(AppNavigation.WeeklyRankingScreen) {
                      inclusive = false
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
