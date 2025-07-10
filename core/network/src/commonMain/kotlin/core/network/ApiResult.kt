package core.network

import com.github.kittinunf.result.Result
import core.models.ApiCallFailure
import core.models.Response

typealias ApiResult<T> = Result<Response<T>, ApiCallFailure>
