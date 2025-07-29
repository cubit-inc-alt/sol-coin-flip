package core.app.di

import org.koin.dsl.module
import core.app.AppUIViewModel
import core.common.AppConfig
import core.features.welcome.WelcomeViewModel
import core.network.di.HttpClientName
import core.sol.WalletAdaptor
import foundation.metaplex.rpc.RPC
import foundation.metaplex.rpc.networking.NetworkDriver
import io.ktor.client.HttpClient
import org.koin.core.qualifier.named


internal val viewModelModule = module {
  factory { AppUIViewModel() }
  factory { WelcomeViewModel() }
}

internal val web3Module = module {
  single {
    RPC(
      rpcUrl = AppConfig.RPC_URL,
      httpNetworkDriver = NetworkDriver(get<HttpClient>(named(HttpClientName.noAuth)))
    )
  }
  single { WalletAdaptor() }
}
