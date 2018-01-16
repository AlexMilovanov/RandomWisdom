package com.alexmilovanov.randomwisdom.view.ui

import android.os.Bundle
import com.alexmilovanov.randomwisdom.R
import dagger.android.support.DaggerAppCompatActivity


class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

    }

}