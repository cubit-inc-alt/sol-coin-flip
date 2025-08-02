package core.data.repository

import core.database.RoomDB
import core.datastore.DataStore
import core.network.RemoteApi


class AuthRepository(
  private val dataStore: DataStore,
  private val remoteApi: RemoteApi,
  private val roomDB: RoomDB,
) {

}
