package com.alexmilovanov.randomwisdom.data.network.retrofit

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Sets headers to original request.
 */
class HeadersInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {

        val original = chain!!.request()

        // Customize the request
        val requestBuilder = original.newBuilder().apply {
            header("Content-Type", "application/json")
        }
        return chain.proceed(requestBuilder.build())
    }

}