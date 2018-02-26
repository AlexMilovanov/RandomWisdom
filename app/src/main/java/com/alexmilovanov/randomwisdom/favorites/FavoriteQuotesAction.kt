package com.alexmilovanov.randomwisdom.favorites

import com.alexmilovanov.randomwisdom.data.persistence.quotes.Quote
import com.alexmilovanov.randomwisdom.mvibase.MviAction

/**
 * Represents restricted class hierarchy for favorite quotes related actions
 */
sealed class FavoriteQuotesAction : MviAction {

    object RequestFavoritesAction : FavoriteQuotesAction()
    data class RemoveFromFavoritesAction(val quote: Quote) : FavoriteQuotesAction()
    data class RestoreInFavoritesAction(val quote: Quote) : FavoriteQuotesAction()
    data class ShareQuoteAction (val quote: Quote) : FavoriteQuotesAction()

}