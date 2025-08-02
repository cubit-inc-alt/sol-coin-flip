package core.network

import com.github.kittinunf.result.Result
import core.models.ApiCallFailure
import core.models.CoinFlip
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import core.models.response.RefreshTokenResponse
import core.network.constants.ApiEndpoints.REFRESH_TOKEN
import core.network.extensions.get


class RemoteApi(
  private val httpClient: HttpClient
) {

  suspend fun refreshToken(token: String, block: HttpRequestBuilder.() -> Unit): ApiResult<RefreshTokenResponse> =
    httpClient.get(path = REFRESH_TOKEN, block = {
      header("Authorization", "Bearer $token")
      block(this)
    })

  suspend fun getPlays(): ApiResult<List<CoinFlip>> = httpClient.get("/coin-flips")
}
