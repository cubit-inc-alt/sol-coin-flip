package core.network.extensions

import com.github.kittinunf.result.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.utils.EmptyContent
import io.ktor.http.ContentType
import io.ktor.http.ContentType.Application
import io.ktor.http.HttpMethod
import io.ktor.http.content.PartData
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import core.analytics.Log
import core.common.CoinFlipConfig
import core.common.inject
import core.common.isDebugBuild
import core.datastore.DataStore
import core.network.constants.ApiEndpoints

internal suspend inline fun <reified Response> HttpClient.request(
    path: String,
    httpMethod: HttpMethod,
    body: Any = EmptyContent,
    requestContentType: ContentType = Application.Json,
    acceptContentType: ContentType = Application.Json,
    queryMap: Map<String, Any?> = emptyMap(),
    crossinline block: HttpRequestBuilder.() -> Unit = {},
) = withContext(Dispatchers.Default ) {
    try {
        val response = request(path) {
            method = httpMethod
            queryMap.forEach { (key, value) -> query(key to value) }
            setBody(body)
            contentType(requestContentType)
            accept(acceptContentType)
            block(this)
        }.body<Response>()
        Result.success(response)
    } catch (e: Exception) {
//    if (isDebugBuild()) {
//      e.printStackTrace()
//    }
        Log.error("HttpClient", e, message = "Failure '${e.message}' on path '$path'")
        Result.failure(e.resolveToFailure())
    }
}


internal suspend inline fun <reified Response> HttpClient.get(
    path: String,
    params: Map<String, Any?> = emptyMap(),
    crossinline block: HttpRequestBuilder.() -> Unit = {},
) = request<Response>(
    path = path,
    httpMethod = HttpMethod.Get,
    body = EmptyContent,
    queryMap = params,
    block = block,
)

internal suspend inline fun <reified Response : Any> HttpClient.post(
    path: String,
    body: Any? = EmptyContent,
    params: Map<String, Any?> = emptyMap(),
    crossinline block: HttpRequestBuilder.() -> Unit = {},
) = request<Response>(
    path = path,
    httpMethod = HttpMethod.Post,
    body = body ?: EmptyContent,
    queryMap = params,
    block = block,
)

internal suspend inline fun <reified Response : Any> HttpClient.option(
    path: String,
    body: Any? = EmptyContent,
    params: Map<String, Any?> = emptyMap(),
    crossinline block: HttpRequestBuilder.() -> Unit = {},
) = request<Response>(
    path = path,
    httpMethod = HttpMethod.Post,
    body = body ?: EmptyContent,
    queryMap = params,
    block = block,
)

internal suspend inline fun <reified Response : Any> HttpClient.patch(
    path: String,
    body: Any = EmptyContent,
    params: Map<String, Any?> = emptyMap(),
    crossinline block: HttpRequestBuilder.() -> Unit = {},
) = request<Response>(
    path = path,
    httpMethod = HttpMethod.Patch,
    body = body,
    queryMap = params,
    block = block,
)

internal suspend inline fun <reified Response : Any> HttpClient.delete(
    path: String,
    body: Any = EmptyContent,
    queryPrams: Map<String, Any?> = emptyMap(),
    crossinline block: HttpRequestBuilder.() -> Unit = {},
) = request<Response>(
    path = path,
    httpMethod = HttpMethod.Delete,
    body = body,
    queryMap = queryPrams,
    block = block,
)

internal suspend inline fun <reified Response : Any> HttpClient.put(
    path: String,
    body: Any = EmptyContent,
    params: Map<String, Any?> = emptyMap(),
    crossinline block: HttpRequestBuilder.() -> Unit = {},
) = request<Response>(
    path = path,
    httpMethod = HttpMethod.Put,
    body = body,
    queryMap = params,
    block = block,
)


internal suspend inline fun <reified Response> HttpClient.uploadMultipart(
    path: String,
    httpMethod: HttpMethod = HttpMethod.Post,
    parts: List<PartData>,
    requestContentType: ContentType = ContentType.MultiPart.FormData,
    acceptContentType: ContentType = Application.Json,
    crossinline block: HttpRequestBuilder.() -> Unit = {},
) = withContext(Dispatchers.IO) {

    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(Logging) {
            level = if (isDebugBuild()) LogLevel.ALL else LogLevel.INFO

            this.logger = object : Logger {
                override fun log(message: String) {
                    Log.info("Ktor", message)
                }
            }
        }
    }
    try {
        val dataStore by inject<DataStore>()
        val response = client.submitFormWithBinaryData(
            url = "${CoinFlipConfig.apiBaseUrl}${ApiEndpoints}rest of api..",
            formData = parts
        ) {
            method = httpMethod
            contentType(requestContentType)
            accept(acceptContentType)

            header("Authorization", "Bearer ${dataStore.accessToken}")
            onUpload { bytesSentTotal, contentLength ->
                Log.info("Bytes sent: $bytesSentTotal / $contentLength")
            }
        }.body<Response>()
        Result.success(response)
    } catch (e: Exception) {
        Log.error("HttpClient", e, message = "Failure '${e.message}' on path '$path'")
        Result.failure(e.resolveToFailure())
    }
}
