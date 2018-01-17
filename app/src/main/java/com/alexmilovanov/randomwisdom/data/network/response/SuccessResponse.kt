package com.alexmilovanov.randomwisdom.data.network.response

import com.google.gson.annotations.SerializedName

/**
 * Represents restricted class hierarchy for success api responses
 */
sealed class SuccessResponse {

    data class QuoteResponse (@SerializedName("quote") val quote: String,
                              @SerializedName("author") val author: String,
                              @SerializedName("cat") val category: String)
    : SuccessResponse()

}