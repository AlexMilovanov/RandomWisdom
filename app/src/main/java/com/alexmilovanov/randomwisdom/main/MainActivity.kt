package com.alexmilovanov.randomwisdom.main

import android.os.Bundle
import com.alexmilovanov.randomwisdom.R
import com.alexmilovanov.randomwisdom.view.BaseActivity
import javax.inject.Inject


/**
 * Displays random quote screen.
 */
class MainActivity : BaseActivity() {

    @Inject
    lateinit var navigator: MainNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        savedInstanceState ?: navigator.navigateToRandomQuotes()
    }

    override fun subscribeToNavigationChanges() {

    }
}