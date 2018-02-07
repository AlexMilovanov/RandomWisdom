package com.alexmilovanov.randomwisdom.splash

import android.content.Intent
import com.alexmilovanov.randomwisdom.R
import com.alexmilovanov.randomwisdom.main.MainActivity
import com.alexmilovanov.randomwisdom.util.log
import com.alexmilovanov.randomwisdom.util.replaceFragmentInActivity
import com.alexmilovanov.randomwisdom.util.startNewActivity
import javax.inject.Inject

/**
 * A utility class that handles navigation in SplashActivity.
 */
class SplashNavigationController
@Inject constructor(val activity: SplashActivity) : SplashNavigator {

    private val containerId = R.id.fl_content

    override fun navigateToSplashScreen() {
        activity.replaceFragmentInActivity(
                SplashFragment(),
                containerId
        )
    }

    override fun navigateToQuotes() {
        val intent = Intent(activity, MainActivity::class.java)
        activity.startNewActivity(intent)
        activity.finish()
    }

}