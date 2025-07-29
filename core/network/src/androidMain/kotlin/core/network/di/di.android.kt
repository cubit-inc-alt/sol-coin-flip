package core.network.di

import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import core.network.baseHttpClient
import io.ktor.client.HttpClient

internal actual fun platformNetworkModule(): Module = module {
    factory(named(HttpClientName.base)) {
        baseHttpClient()
    }
}
