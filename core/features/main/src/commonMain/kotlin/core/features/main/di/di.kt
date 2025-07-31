package core.features.main.di

import core.data.repository.PlayRepository
import core.features.WeeklyRanking.ranking.WeeklyRankingScreenViewModel
import core.features.main.MainScreenViewModel
import org.koin.dsl.module


fun mainModule() = module {
  factory { MainScreenViewModel(get<PlayRepository>()) }
  factory { WeeklyRankingScreenViewModel(get<PlayRepository>()) }
}
