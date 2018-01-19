package com.alexmilovanov.randomwisdom.randomquote

import com.alexmilovanov.randomwisdom.mvibase.MviIntent

/**
 * Represents restricted class hierarchy for random quote related intents
 */
sealed class RandomQuoteIntent : MviIntent {

    object InitialIntent : RandomQuoteIntent()
    object NextQuoteIntent : RandomQuoteIntent()
}