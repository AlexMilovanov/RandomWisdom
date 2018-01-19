package com.alexmilovanov.randomwisdom.randomquote

import com.alexmilovanov.randomwisdom.mvibase.MviAction

/**
 * Represents restricted class hierarchy for random quote related actions
 */
sealed class RandomQuoteAction : MviAction {

    object RequestNextQuoteAction : RandomQuoteAction()
}