package com.alexmilovanov.randomwisdom.ui.splash

import android.content.Intent
import com.alexmilovanov.randomwisdom.R
import com.alexmilovanov.randomwisdom.ui.main.MainActivity
import com.alexmilovanov.randomwisdom.util.ext.replaceFragmentInActivity
import com.alexmilovanov.randomwisdom.util.ext.showActionSnackbar
import com.alexmilovanov.randomwisdom.util.resources.IResourceProvider
import com.alexmilovanov.randomwisdom.util.ext.startNewActivity
import io.reactivex.Maybe
import kotlinx.android.synthetic.main.activity_splash.*
import javax.inject.Inject

/**
 * A utility class that handles navigation in SplashActivity.
 */
class SplashNavigationController
@Inject constructor(val activity: SplashActivity, val resProvider: IResourceProvider) : SplashNavigator {

    private val containerId = R.id.fl_content

    override fun navigateToSplashScreen() {
        activity.replaceFragmentInActivity(
                SplashFragment.invoke(),
                containerId
        )
    }

    override fun navigateToQuotes() {
        val intent = Intent(activity, MainActivity::class.java)
        activity.apply {
            startNewActivity(intent)
            finish()
        }
    }

    override fun showErrorWithRetry(errorMsg: String): Maybe<Boolean> {
        return activity.showActionSnackbar(
                activity.coordinator_layout, resProvider, errorMsg,
                actionTitle = resProvider.string(R.string.button_retry_text)
        )
    }
}
