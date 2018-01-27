package com.alexmilovanov.randomwisdom.view

import android.os.Bundle
import dagger.android.support.DaggerFragment

/**
 * Holds common functions and properties
 */
abstract class BaseFragment <VM : BaseViewModel> : DaggerFragment() {

    lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribeToNavigationChanges()
    }

    abstract fun initViewModel()

    abstract fun subscribeToNavigationChanges()

}