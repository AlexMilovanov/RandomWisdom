package com.alexmilovanov.randomwisdom.randomquote

import com.alexmilovanov.randomwisdom.data.persistence.quotes.Quote
import com.alexmilovanov.randomwisdom.mvibase.MviResult

/**
 * Represents restricted class hierarchy for random quote related results
 */
sealed class RandomQuoteResult : MviResult {

    sealed class RequestNextQuoteResult : RandomQuoteResult() {
        data class Success(val quote: Quote) : RequestNextQuoteResult()
        data class Failure(val error: Throwable) : RequestNextQuoteResult()
        object InFlight : RequestNextQuoteResult()
    }
}