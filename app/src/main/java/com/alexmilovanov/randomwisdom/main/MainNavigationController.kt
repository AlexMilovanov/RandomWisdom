package com.alexmilovanov.randomwisdom.main

import com.alexmilovanov.randomwisdom.R
import com.alexmilovanov.randomwisdom.randomquote.RandomQuoteFragment
import com.alexmilovanov.randomwisdom.util.replaceFragmentInActivity
import javax.inject.Inject

/**
 * A utility class that handles navigation in MainActivity.
 */
class MainNavigationController
@Inject constructor(val activity: MainActivity) : MainNavigator {

    private val containerId = R.id.fl_content

    override fun navigateToRandomQuotes() {
        activity.replaceFragmentInActivity(
                RandomQuoteFragment(),
                containerId
        )
    }

}