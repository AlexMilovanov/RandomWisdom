package com.alexmilovanov.randomwisdom.splash

import com.alexmilovanov.randomwisdom.mvibase.MviAction


/**
 * Represents restricted class hierarchy for app initialization related actions
 */
sealed class AppLaunchAction : MviAction {

    object RequestInitialQuotesAction : AppLaunchAction()
}