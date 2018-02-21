package com.alexmilovanov.randomwisdom.main

import io.reactivex.Maybe

/**
 * Defines the navigation actions that can be called from the Main Activity screen.
 */
interface MainNavigator {

    fun navigateToRandomQuotes()

    fun navigateToFavorites()

    fun shareText(text: String)

    fun showErrorWithRetry(errorMsg: String) : Maybe<Boolean>
}