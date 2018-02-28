package com.alexmilovanov.randomwisdom.ui.main

import android.support.design.widget.Snackbar
import com.alexmilovanov.randomwisdom.R
import com.alexmilovanov.randomwisdom.ui.randomquote.RandomQuoteFragment
import com.alexmilovanov.randomwisdom.util.ext.replaceFragmentInActivity
import javax.inject.Inject
import android.support.v4.app.ShareCompat
import com.alexmilovanov.randomwisdom.ui.favorites.FavoriteQuotesFragment
import com.alexmilovanov.randomwisdom.util.ext.showActionSnackbar
import com.alexmilovanov.randomwisdom.util.ext.startNewActivity
import com.alexmilovanov.randomwisdom.util.resources.IResourceProvider
import io.reactivex.Maybe
import kotlinx.android.synthetic.main.activity_splash.*


/**
 * A utility class that handles navigation in MainActivity.
 */
class MainNavigationController
@Inject constructor(val activity: MainActivity, val resProvider: IResourceProvider) : MainNavigator {

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

    override fun showErrorWithRetry(errorMsg: String): Maybe<Boolean> {
        return activity.showActionSnackbar(
                layout = activity.coordinator_layout,
                resProvider = resProvider,
                msg = errorMsg,
                actionTitle = resProvider.string(R.string.button_retry_text)
        )
    }

    override fun showNotificationWithAction(msg: String): Maybe<Boolean> {
        return activity.showActionSnackbar(
                activity.coordinator_layout,
                resProvider,
                msg = msg,
                length = Snackbar.LENGTH_LONG,
                isError = false,
                actionTitle = resProvider.string(R.string.button_undo_text)
        )
    }

}