package com.alexmilovanov.randomwisdom.splash

import io.reactivex.Maybe

/**
 * Defines the navigation actions that can be called from the Splash screen.
 */
interface SplashNavigator {

    fun navigateToSplashScreen()

    fun navigateToQuotes()

    fun showErrorWithRetry(errorMsg: String) : Maybe<Boolean>
}