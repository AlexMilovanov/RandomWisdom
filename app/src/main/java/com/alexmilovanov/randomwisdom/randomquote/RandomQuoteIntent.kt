package com.alexmilovanov.randomwisdom.randomquote

import com.alexmilovanov.randomwisdom.data.persistence.quotes.Quote
import com.alexmilovanov.randomwisdom.mvibase.MviIntent

/**
 * Represents restricted class hierarchy for random quote related intents
 */
sealed class RandomQuoteIntent : MviIntent {

    object InitialIntent : RandomQuoteIntent()
    object NextQuoteIntent : RandomQuoteIntent()
    data class LikeCurrentQuoteIntent (val quote: Quote) : RandomQuoteIntent()
    data class ShareCurrentQuoteIntent (val quote: Quote) : RandomQuoteIntent()

}