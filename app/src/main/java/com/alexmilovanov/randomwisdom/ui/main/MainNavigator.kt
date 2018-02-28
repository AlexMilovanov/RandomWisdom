package com.alexmilovanov.randomwisdom.ui.main

import io.reactivex.Maybe

/**
 * Defines the navigation actions that can be called from the Main Activity screen.
 */
interface MainNavigator {

    fun navigateToRandomQuotes()

    fun navigateToFavorites()

    fun shareText(text: String)

    fun showErrorWithRetry(errorMsg: String) : Maybe<Boolean>

    fun showNotificationWithAction(msg: String) : Maybe<Boolean>
}