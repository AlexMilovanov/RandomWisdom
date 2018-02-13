package com.alexmilovanov.randomwisdom.favorites

import com.alexmilovanov.randomwisdom.mvibase.MviAction

/**
 * Represents restricted class hierarchy for favorite quotes related actions
 */
sealed class FavoriteQuotesAction : MviAction {

    object RequestFavoritesAction : FavoriteQuotesAction()
}