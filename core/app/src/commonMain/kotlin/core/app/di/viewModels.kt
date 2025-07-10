package core.app.di

import org.koin.dsl.module
import core.app.AppUIViewModel
import core.data.repository.AuthRepository


internal fun viewModelModule() = module {
    factory { AppUIViewModel(get<AuthRepository>()) }
}
