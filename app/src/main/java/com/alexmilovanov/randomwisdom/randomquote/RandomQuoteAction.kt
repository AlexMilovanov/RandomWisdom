package com.alexmilovanov.randomwisdom.randomquote

import com.alexmilovanov.randomwisdom.data.persistence.quotes.Quote
import com.alexmilovanov.randomwisdom.mvibase.MviAction

/**
 * Represents restricted class hierarchy for random quote related actions
 */
sealed class RandomQuoteAction : MviAction {

    object RequestNextQuoteAction : RandomQuoteAction()
    data class LikeQuoteAction (val quote: Quote) : RandomQuoteAction()
    data class ShareQuoteAction (val quote: Quote) : RandomQuoteAction()

}