package com.alexmilovanov.randomwisdom.view.randomquote

import android.os.Bundle
import com.alexmilovanov.randomwisdom.R
import dagger.android.support.DaggerAppCompatActivity


/**
 * Displays random quote screen.
 */
class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}