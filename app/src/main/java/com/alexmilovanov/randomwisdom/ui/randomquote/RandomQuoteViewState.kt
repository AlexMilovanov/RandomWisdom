package com.alexmilovanov.randomwisdom.ui.randomquote

import com.alexmilovanov.randomwisdom.data.persistence.quotes.Quote
import com.alexmilovanov.randomwisdom.mvibase.MviViewState

/**
 * Represents possible random quote screen view states
 */
data class RandomQuoteViewState (
        val quote: Quote?,
        val loading: Boolean,
        val error: Throwable?
) : MviViewState {

    companion object {
        fun idle() : RandomQuoteViewState {
            return RandomQuoteViewState (
                    null,
                    false,
                    null
            )
        }
    }

}