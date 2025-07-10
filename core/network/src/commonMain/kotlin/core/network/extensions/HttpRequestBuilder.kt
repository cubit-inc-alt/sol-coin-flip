package core.network.extensions

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter

internal fun HttpRequestBuilder.query(vararg params: Pair<String, *>) {
    params.forEach { (key, value) ->
        if (value is Collection<*>) {
            value.forEach { query(key to it) }
        } else {
            parameter(key, value)
        }
    }
}
