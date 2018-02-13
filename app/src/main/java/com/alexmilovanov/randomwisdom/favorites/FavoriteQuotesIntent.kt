package com.alexmilovanov.randomwisdom.favorites

import com.alexmilovanov.randomwisdom.mvibase.MviIntent

/**
 * Represents restricted class hierarchy for favorite quotes related intents
 */
sealed class FavoriteQuotesIntent : MviIntent {

    object InitialIntent : FavoriteQuotesIntent()

}