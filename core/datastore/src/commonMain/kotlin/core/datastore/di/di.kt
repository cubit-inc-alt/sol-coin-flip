package core.datastore.di

import org.koin.dsl.module
import core.datastore.DataStore

fun dataStoreModule() = module {
    single {
        DataStore()
    }
}
