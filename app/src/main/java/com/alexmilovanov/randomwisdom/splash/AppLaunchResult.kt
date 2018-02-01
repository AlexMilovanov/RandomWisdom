package com.alexmilovanov.randomwisdom.splash

import com.alexmilovanov.randomwisdom.mvibase.MviResult

/**
 * Represents restricted class hierarchy for app initialization results
 */
sealed class AppLaunchResult : MviResult {

    sealed class InitialQuotesResult : AppLaunchResult() {
        object Success: InitialQuotesResult()
        data class Failure(val error: Throwable) : InitialQuotesResult()
        object InFlight : InitialQuotesResult()
    }
}