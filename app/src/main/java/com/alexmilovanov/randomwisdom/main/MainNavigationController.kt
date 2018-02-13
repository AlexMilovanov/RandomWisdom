package com.alexmilovanov.randomwisdom.main

import com.alexmilovanov.randomwisdom.R
import com.alexmilovanov.randomwisdom.randomquote.RandomQuoteFragment
import com.alexmilovanov.randomwisdom.util.ext.replaceFragmentInActivity
import javax.inject.Inject
import android.support.v4.app.ShareCompat
import com.alexmilovanov.randomwisdom.favorites.FavoriteQuotesFragment
import com.alexmilovanov.randomwisdom.util.ext.startNewActivity


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

    override fun navigateToFavorites() {
        activity.replaceFragmentInActivity(
                FavoriteQuotesFragment(),
                containerId
        )
    }

    override fun shareText(text: String) {
        val sharingIntent = ShareCompat.IntentBuilder.from(activity)
                .setType("text/plain")
                .setText(text)
                .intent
        if (sharingIntent.resolveActivity(activity.packageManager) != null) {
            activity.startNewActivity(sharingIntent)
        }
    }

}