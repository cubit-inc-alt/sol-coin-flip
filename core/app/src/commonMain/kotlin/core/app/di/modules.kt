package core.app.di

import org.koin.dsl.module
import core.app.AppUIViewModel
import core.common.AppConfig
import core.features.welcome.WelcomeViewModel
import core.sol.WalletAdaptor
import foundation.metaplex.rpc.RPC


internal val viewModelModule = module {
  factory { AppUIViewModel() }
  factory { WelcomeViewModel() }
}

internal val web3Module = module {
  single { RPC(AppConfig.RPC_URL) }
  single { WalletAdaptor() }
}
