package com.alexmilovanov.randomwisdom.view

import dagger.android.support.DaggerAppCompatActivity

/**
 * Holds methods and variables common for all activities
 */
abstract class BaseActivity : DaggerAppCompatActivity() {

    override fun onStart() {
        super.onStart()
        subscribeToNavigationChanges()
    }

    abstract fun subscribeToNavigationChanges()

}