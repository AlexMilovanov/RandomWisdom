package com.alexmilovanov.randomwisdom.data.network.retrofit

import com.alexmilovanov.randomwisdom.R
import com.alexmilovanov.randomwisdom.errorhandling.HttpException
import com.alexmilovanov.randomwisdom.util.resources.IResourceProvider
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Class that implements Interceptor to perform http error codes mapping to readable error messages
 */
class ErrorInterceptor @Inject constructor(private val resProvider: IResourceProvider) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val response = chain.proceed(request)

        if(response.isSuccessful && response.code() in 200..299) return response

        val errorMsgRes = when (response.code()) {
            304 -> R.string.error_msg_http_304
            400 -> R.string.error_msg_http_400
            401 -> R.string.error_msg_http_401
            403 -> R.string.error_msg_http_403
            404 -> R.string.error_msg_http_404
            406 -> R.string.error_msg_http_406
            410 -> R.string.error_msg_http_410
            420 -> R.string.error_msg_http_420
            422 -> R.string.error_msg_http_422
            429 -> R.string.error_msg_http_429
            500 -> R.string.error_msg_http_500
            502 -> R.string.error_msg_http_502
            503 -> R.string.error_msg_http_503
            504 -> R.string.error_msg_http_504
            else -> R.string.error_msg_unexpected_error
        }

        throw HttpException(resProvider.string(errorMsgRes))
    }

}