package com.alexmilovanov.randomwisdom.view

import android.os.Bundle
import android.view.View
import com.alexmilovanov.randomwisdom.mvibase.MviIntent
import com.alexmilovanov.randomwisdom.mvibase.MviView
import com.alexmilovanov.randomwisdom.mvibase.MviViewModel
import com.alexmilovanov.randomwisdom.mvibase.MviViewState
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable

/**
 * Holds common functions and properties
 */
abstract class BaseFragment <VM : BaseViewModel<I, VS>, I: MviIntent, VS: MviViewState>
    : DaggerFragment(), MviView<I, VS> {

    protected var mDisposables = CompositeDisposable()

    lateinit var mViewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposables.dispose()
    }

    /**
     * Connect the [MviView] with the [MviViewModel]
     * We subscribe to the [MviViewModel] before passing it the [MviView]'s [MviIntent]s.
     * If we were to pass [MviIntent]s to the [MviViewModel] before listening to it,
     * emitted [MviViewState]s could be lost
     */
    private fun bind() {
        mViewModel = initViewModel()
        // Subscribe to the ViewModel and call render for every emitted state
        mDisposables.add(mViewModel.states().subscribe{state -> render(state)})
        // Pass the UI's intents to the ViewModel
        mViewModel.processIntents(intents())
    }

    /**
     * Initialize an appropriate ViewModel
     */
    abstract fun initViewModel(): VM

}