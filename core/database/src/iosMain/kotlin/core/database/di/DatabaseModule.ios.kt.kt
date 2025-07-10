package core.database.di

import org.koin.dsl.module
import core.database.RoomDB
import core.database.getDatabaseBuilder


actual fun databaseModule() = module {
    single<RoomDB> { getDatabaseBuilder() }
    includes(daoModules())
}
