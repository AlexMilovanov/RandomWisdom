package com.alexmilovanov.randomwisdom.favorites

import com.alexmilovanov.randomwisdom.data.persistence.quotes.Quote
import com.alexmilovanov.randomwisdom.mvibase.MviIntent

/**
 * Represents restricted class hierarchy for favorite quotes related intents
 */
sealed class FavoriteQuotesIntent : MviIntent {

    object InitialIntent : FavoriteQuotesIntent()
    data class DeleteQuoteIntent(val quote: Quote) : FavoriteQuotesIntent()
    data class RestoreQuoteIntent(val quote: Quote) : FavoriteQuotesIntent()
}