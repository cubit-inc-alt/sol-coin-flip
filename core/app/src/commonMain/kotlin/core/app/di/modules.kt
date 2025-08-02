package core.app.di

import org.koin.dsl.module
import core.app.AppUIViewModel
import core.common.AppConfig
import core.datastore.DataStore
import core.features.welcome.WelcomeViewModel
import core.network.di.HttpClientName
import core.sol.ConnectionStatus
import core.sol.WalletAdaptor
import core.sol.nacl.Nacl
import foundation.metaplex.base58.decodeBase58
import foundation.metaplex.base58.encodeToBase58String
import foundation.metaplex.rpc.RPC
import foundation.metaplex.rpc.networking.NetworkDriver
import io.ktor.client.HttpClient
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope


internal val viewModelModule = module {
  factory { AppUIViewModel(get<DataStore>()) }
  factory { WelcomeViewModel() }
}

internal val web3Module = module {
  single {
    RPC(
      rpcUrl = AppConfig.RPC_URL,
      httpNetworkDriver = NetworkDriver(get<HttpClient>(named(HttpClientName.noAuth)))
    )
  }
  single { walletAdaptor() }
}


private fun Scope.walletAdaptor(): WalletAdaptor {
  val dataStore = get<DataStore>()

  val onWalletConnected = { keyPair: Nacl.KeyPair, connected: ConnectionStatus.Connected ->
    dataStore.dAppPublicKey = keyPair.publicKey.encodeToBase58String()
    dataStore.dAppPrivateKey = keyPair.secretKey.encodeToBase58String()
    dataStore.sharedSecret = connected.sharedSecret.encodeToBase58String()
    dataStore.session = connected.session
    dataStore.userAccountPublicKey = connected.userAccountPublicKey
    dataStore.termsAccepted = true
  }

  try {
    if (dataStore.dAppPrivateKey != null && dataStore.dAppPublicKey != null) {
      val secretKey = dataStore.dAppPrivateKey!!.decodeBase58()
      val publicKey = dataStore.dAppPublicKey!!.decodeBase58()
      val keyPair = Nacl.KeyPair(publicKey = publicKey, secretKey = secretKey)

      return WalletAdaptor(
        dAppKeyPair = keyPair,
        connectionStatus = ConnectionStatus.Connected(
          sharedSecret = dataStore.sharedSecret!!.decodeBase58(),
          session = dataStore.session!!,
          userAccountPublicKey = dataStore.userAccountPublicKey!!,
        ),
        onConnected = onWalletConnected
      )
    }
  } catch (e: Exception) {
    e.printStackTrace()
  }

  val kyPair = Nacl.Box.keyPair()

  return WalletAdaptor(dAppKeyPair = kyPair, onConnected = onWalletConnected)

}
