package core.app.di

import org.koin.dsl.module
import core.data.di.dataModule
import core.features.main.di.mainModule
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
fun appModule() = module {
  includes(
    dataModule(),
    viewModelModule(),
    mainModule()
  )
}
