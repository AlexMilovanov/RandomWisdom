package com.alexmilovanov.randomwisdom.ui.splash

import com.alexmilovanov.randomwisdom.mvibase.MviIntent

/**
 * Represents restricted class hierarchy for intents triggered from splash screen
 */
sealed class AppLaunchIntent : MviIntent {

    object InitialQuotesIntent : AppLaunchIntent()
    object RetryIntent : AppLaunchIntent()

}