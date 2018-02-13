package com.alexmilovanov.randomwisdom.main

import android.os.Bundle
import com.alexmilovanov.randomwisdom.R
import com.alexmilovanov.randomwisdom.uicommon.BaseActivity
import com.alexmilovanov.randomwisdom.util.ext.finishWithFadeOut
import kotlinx.android.synthetic.main.activity_main.*
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
        setSupportActionBar(toolbar)

        savedInstanceState ?: navigator.navigateToRandomQuotes()
    }

    override fun onBackPressed() {
        finishWithFadeOut()
    }

}