package com.alexmilovanov.randomwisdom.data.network

import com.alexmilovanov.randomwisdom.data.ApiConstants.Companion.CACHED_QUOTES_PATH
import com.alexmilovanov.randomwisdom.data.network.response.SuccessResponse
import io.reactivex.Single
import retrofit2.http.GET

/**
 * Interface for Retrofit library that facilitates making REST calls to the server
 * by defining HTTP methods using annotations.
 */
interface ApiService {

    @GET(CACHED_QUOTES_PATH)
    fun getCachedQuotes(): Single<List<SuccessResponse.QuoteResponse>>

}