package core.network.di

import core.datastore.DataStore
import core.network.AppRemoteApi
import core.network.AppRemoteApiImpl
import core.network.BearerTokenRefresher
import core.network.baseHttpClient
import core.network.buildAuthHttpClient
import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import core.network.buildNoAuthHttpClient

@Suppress("ConstPropertyName")
object HttpClientName {
  const val base = "base-http-client"
  const val noAuth = "no-auth-http-client"
  const val auth = "auth-http-client"
}


fun networkModule(): Module = module {
  includes(platformNetworkModule())

  single(named(HttpClientName.base)) {
    baseHttpClient()
  }

  single(named(HttpClientName.auth)) {
    buildAuthHttpClient(get<HttpClient>(named(HttpClientName.base)), get<DataStore>())
  }

  single(named(HttpClientName.noAuth)) {
    buildNoAuthHttpClient(get<HttpClient>(named(HttpClientName.base)))
  }

  single<AppRemoteApi> {
    AppRemoteApiImpl(get<HttpClient>(named(HttpClientName.auth)))
  }

  singleOf(::BearerTokenRefresher)
}

internal expect fun platformNetworkModule(): Module



