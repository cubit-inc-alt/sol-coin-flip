package core.network.di

import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import core.network.baseHttpClient

internal actual fun platformNetworkModule(): Module = module {
    factory(named(HttpClientName.base)) {
        baseHttpClient()
    }
}
