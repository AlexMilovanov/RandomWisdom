package com.alexmilovanov.randomwisdom.ui.favorites

import com.alexmilovanov.randomwisdom.data.persistence.quotes.Quote
import com.alexmilovanov.randomwisdom.mvibase.MviViewState


/**
 * Represents possible favorite quotes screen view states
 */
data class FavoriteQuotesViewState (
        val favorites: List<Quote>?,
        val loading: Boolean,
        val error: Throwable?
): MviViewState {

    companion object {
        fun idle() : FavoriteQuotesViewState {
            return FavoriteQuotesViewState (
                    null,
                    false,
                    null
            )
        }
    }

}