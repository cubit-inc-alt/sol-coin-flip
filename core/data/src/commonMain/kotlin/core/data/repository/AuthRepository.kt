package core.data.repository

import core.database.RoomDB
import core.datastore.DataStore
import core.network.AppRemoteApi
import kotlinx.coroutines.flow.Flow


class AuthRepository(
    private val dataStore: DataStore,
    private val remoteApi: AppRemoteApi,
    private val roomDB: RoomDB,
) {
    fun getIsLoggedIn(): Boolean {
        return dataStore.isLoggedIn == true
    }

    fun getUserLoggedInFlow(): Flow<Boolean> {
        return dataStore.getBooleanFlow(DataStore.Companion.IS_LOGGED_IN)
    }
}
