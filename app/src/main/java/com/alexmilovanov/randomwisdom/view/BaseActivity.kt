package com.alexmilovanov.randomwisdom.view

import com.alexmilovanov.randomwisdom.util.overridePendingTransitionExit
import dagger.android.support.DaggerAppCompatActivity

/**
 * Holds methods and variables common for all activities
 */
abstract class BaseActivity : DaggerAppCompatActivity() {

    override fun onStart() {
        super.onStart()
        subscribeToNavigationChanges()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransitionExit()
    }

    abstract fun subscribeToNavigationChanges()

}