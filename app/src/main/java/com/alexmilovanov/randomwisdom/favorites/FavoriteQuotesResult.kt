package com.alexmilovanov.randomwisdom.favorites

import com.alexmilovanov.randomwisdom.data.persistence.quotes.Quote
import com.alexmilovanov.randomwisdom.mvibase.MviResult


/**
 * Represents restricted class hierarchy for favorite quotes related results
 */
sealed class FavoriteQuotesResult : MviResult {

    sealed class RequestFavoritesResult : FavoriteQuotesResult() {
        data class Success(val quotes: List<Quote>) : RequestFavoritesResult()
        data class Failure(val error: Throwable) : RequestFavoritesResult()
        object InFlight : RequestFavoritesResult()
    }

    sealed class RemoveFromFavoritesResult : FavoriteQuotesResult() {
        data class Success (val quote: Quote) : RemoveFromFavoritesResult()
        data class Failure(val error: Throwable) : RemoveFromFavoritesResult()
    }

    sealed class AddToFavoritesResult : FavoriteQuotesResult() {
        data class Success (val quote: Quote) : AddToFavoritesResult()
        data class Failure(val error: Throwable) : AddToFavoritesResult()
    }
}
