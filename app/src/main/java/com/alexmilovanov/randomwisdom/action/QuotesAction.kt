package com.alexmilovanov.randomwisdom.action

import com.alexmilovanov.randomwisdom.data.persistence.quotes.Quote
import com.alexmilovanov.randomwisdom.mvibase.MviAction


/**
 * Represents restricted class hierarchy for quotes related actions
 */
sealed class QuotesAction : MviAction {

    object RequestInitialQuotesAction : QuotesAction()
    object RequestNextQuoteAction : QuotesAction()
    object RequestFavoritesAction : QuotesAction()
    data class ShareQuoteAction (val quote: Quote) : QuotesAction()
    data class RemoveFromFavoritesAction(val quote: Quote) : QuotesAction()
    data class AddToFavoritesAction(val quote: Quote) : QuotesAction()

}