package com.alexmilovanov.randomwisdom.splash

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import com.alexmilovanov.randomwisdom.view.BaseFragment
import javax.inject.Inject

/**
* Main UI for a splash screen
*/
class SplashFragment : BaseFragment<SplashViewModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navigator: SplashNavigator


    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SplashViewModel::class.java)
    }

    override fun subscribeToNavigationChanges() {

    }

}