package app.android

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import core.app.di.appModule
import core.app.onAppStarted

class PandaApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    onAppStarted()
    startKoin {
      androidContext(this@PandaApplication)
      modules(
        appModule()
      )
    }
  }

}


//https://phantom.app/ul/v1connect,dapp_encryption_public_key=2Qpz1GfKJpSYu4szAiBG4kuQedhegPvsRqddqrvMNe9m,app_url=https://cubit.com.np,redirect_link=flipper://wallet/connect/d5a74724-ae86-4809-a662-7b203fcdd5a5
// flipper://wallet/connect/1d688c36-9020-4e15-8e19-f2efb5e4a402?phantom_encryption_public_key=5SA7ofb9ZMhvKks6UGgDhLvhFYDxJkPsQFWccewpFqEK&nonce=Js8QkwVVqoZ3XYE9k73fmfuHgmdiXc8k8&data=4HBjT7jtEe3KmtgWt4UxCpooRWqbgCSHhbTavRvp4M9GgKfG5rLYPHN1s6rDeeVvQqgBRxhKF7dQoJjrcmeDeQVSUygh42juNLFUje9HZbGtPo9cUumq4XFqwqp1QockXnhA1p387ysurC4re2z69WvivQCZjXd21cAxSNAQqhcE5Pbbu1b76KJtXxLtf69jh3gcgX5wcwVPMvGKb6kM15Lc9h49PM1f8uMJfuQwCN1CyJGYmu4aHFBVbp1SkSnDiu2ZdcguPhN6nZZpKrR4ktDaf92hsSyo32YLGFJoBvhoeaRWMaYSxfSt1uANmNGwavoN7LWaiF9zVkWE7WkxrdRs2RDRbbYCkVFh4o48FmoRgN4gRycWAyo4f2JvuW5sDUiRFT3RixUVhMzaebkULQwPnyW3Yo3jh5EybVtKJJ58JFqw
