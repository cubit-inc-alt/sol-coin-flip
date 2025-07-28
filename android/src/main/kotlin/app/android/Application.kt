package app.android

import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import core.app.di.appModule
import core.app.onAppStarted

class Application : android.app.Application() {
  override fun onCreate() {
    super.onCreate()
    onAppStarted()

    startKoin {
      androidContext(applicationContext)
      modules(
        appModule()
      )
    }
  }
}
