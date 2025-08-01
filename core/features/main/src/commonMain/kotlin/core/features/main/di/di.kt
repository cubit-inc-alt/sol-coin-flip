package core.features.main.di

import core.data.repository.PlayRepository
import core.features.WeeklyRanking.ranking.WeeklyRankingScreenViewModel
import core.features.main.MainScreenViewModel
import core.sol.WalletAdaptor
import org.koin.dsl.module

fun mainModule() = module {
  factory { MainScreenViewModel(
    get<PlayRepository>(),
    walletAdaptor = get<WalletAdaptor>()
  ) }
  factory { WeeklyRankingScreenViewModel(get<PlayRepository>()) }
}
