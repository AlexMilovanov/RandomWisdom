package com.alexmilovanov.randomwisdom.result

import com.alexmilovanov.randomwisdom.data.persistence.quotes.Quote
import com.alexmilovanov.randomwisdom.mvibase.MviResult

/**
 * Represents restricted class hierarchy for quotes related results
 */
sealed class QuotesResult : MviResult {

    sealed class InitialQuotesResult : QuotesResult() {
        object Success: InitialQuotesResult()
        data class Failure(val error: Throwable) : InitialQuotesResult()
        object InFlight : InitialQuotesResult()
    }

    sealed class RequestNextQuoteResult : QuotesResult() {
        data class Success(val quote: Quote) : RequestNextQuoteResult()
        data class Failure(val error: Throwable) : RequestNextQuoteResult()
        object InFlight : RequestNextQuoteResult()
    }

    sealed class ShareQuoteResult : QuotesResult() {
        data class Success(val text: String) : ShareQuoteResult()
        data class Failure(val error: Throwable) : ShareQuoteResult()
    }

    sealed class RequestFavoritesResult : QuotesResult() {
        data class Success(val quotes: List<Quote>) : RequestFavoritesResult()
        data class Failure(val error: Throwable) : RequestFavoritesResult()
        object InFlight : RequestFavoritesResult()
    }

    sealed class RemoveFromFavoritesResult : QuotesResult() {
        data class Success (val quote: Quote) : RemoveFromFavoritesResult()
        data class Failure(val error: Throwable) : RemoveFromFavoritesResult()
    }

    sealed class AddToFavoritesResult : QuotesResult() {
        data class Success (val quote: Quote) : AddToFavoritesResult()
        data class Failure(val error: Throwable) : AddToFavoritesResult()
    }

}