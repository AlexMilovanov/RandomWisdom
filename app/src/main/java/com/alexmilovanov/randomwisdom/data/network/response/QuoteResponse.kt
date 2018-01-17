package com.alexmilovanov.randomwisdom.data.network.response

import com.google.gson.annotations.SerializedName

/**
 * Reflects api response on a single quote
 */
data class QuoteResponse (@SerializedName("quote") val quote: String,
                          @SerializedName("author") val author: String,
                          @SerializedName("cat") val category: String)