package com.alexmilovanov.randomwisdom.randomquote

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import com.alexmilovanov.randomwisdom.view.BaseFragment
import javax.inject.Inject


/**
 * Main UI for the random quote screen.
 */
class RandomQuoteFragment : BaseFragment <RandomQuoteViewModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RandomQuoteViewModel::class.java)
    }

    override fun subscribeToNavigationChanges() {

    }

}