package core.database.di

import android.content.Context
import org.koin.dsl.module
import core.database.RoomDB
import core.database.getDatabaseBuilder


actual fun databaseModule() = module {
    single<RoomDB> { getDatabaseBuilder(get<Context>()) }
    includes(daoModules())
}
