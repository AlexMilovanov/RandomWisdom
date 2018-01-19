package com.alexmilovanov.randomwisdom.randomquote

import com.alexmilovanov.randomwisdom.mvibase.MviViewState

/**
 * Represents possible random quote screen view states
 */
data class RandomQuoteViewState (
        val quoteText: String,
        val author: String,
        val loading: Boolean,
        val error: Throwable?
) : MviViewState {

    companion object {
        fun idle() : RandomQuoteViewState {
            return RandomQuoteViewState (
                    "",
                    "",
                    false,
                    null
            )
        }
    }

}