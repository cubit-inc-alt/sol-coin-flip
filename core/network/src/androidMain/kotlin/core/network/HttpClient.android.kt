package core.network

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import me.sujanpoudel.utils.contextProvider.applicationContext

internal actual fun baseHttpClient() = HttpClient(OkHttp) {
    engine {
        config {
            addInterceptor(chuckerInterceptor())
        }
    }
}

private fun chuckerInterceptor() = ChuckerInterceptor.Builder(applicationContext)
    .collector(ChuckerCollector(applicationContext))
    .maxContentLength(250000L)
    .redactHeaders(emptySet())
    .alwaysReadResponseBody(false)
    .build()
