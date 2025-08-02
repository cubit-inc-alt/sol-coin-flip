package core.data.di

import core.data.repository.AuthRepository
import core.data.repository.PlayRepository
import core.database.RoomDB
import core.database.di.databaseModule
import core.datastore.DataStore
import core.datastore.di.dataStoreModule
import core.network.RemoteApi
import core.network.di.networkModule
import org.koin.core.module.Module
import org.koin.dsl.module

fun dataModule(): Module = module {
  includes(
    databaseModule(),
    networkModule(),
    dataStoreModule(),
  )

  single<AuthRepository> {
    AuthRepository(
      get<DataStore>(),
      get<RemoteApi>(),
      get<RoomDB>(),
    )
  }

  single<PlayRepository> {
    PlayRepository(
    )
  }
}


