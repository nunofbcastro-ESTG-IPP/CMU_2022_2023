package pt.ipp.estg.cmu_07_8200398_8200591_8200592.network

import retrofit2.Response
import java.net.SocketTimeoutException

sealed class NetworkResult<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?) : NetworkResult<T>(data)
    class Error<T>(message: String, data: T? = null) : NetworkResult<T>(data, message)
}

private fun <T> verifyResultNetwork(
    response: Response<T>,
    defaultData: T? = null
): NetworkResult<T> {
    if (response.isSuccessful) {
        response.body()?.let {
            return NetworkResult.Success(
                data = response.body()
            )
        }
    }

    return NetworkResult.Error(
        message = response.message(),
        data = defaultData
    )
}

suspend fun <T> executeRequest(
    request: suspend () -> Response<T>
): NetworkResult<T> {
    return try {
        verifyResultNetwork(
            request()
        )
    } catch (e: SocketTimeoutException) {
        NetworkResult.Error(message = "Timeout Exception")

    } catch (e: Exception) {
        NetworkResult.Error(
            message = "Couldn't reach server, check your internet connection"
        )
    }
}

fun verifyURL(
    url: String
): String {
    if (!url.contains("http://") || !url.contains("https://")) {
        return "http://$url"
    }
    return url
}