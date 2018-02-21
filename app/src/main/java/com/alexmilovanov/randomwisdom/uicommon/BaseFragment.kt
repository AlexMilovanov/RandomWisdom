package com.alexmilovanov.randomwisdom.uicommon

import android.os.Bundle
import android.view.View
import com.alexmilovanov.randomwisdom.mvibase.*
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable

/**
 * Holds common functions and properties
 */
abstract class BaseFragment <VM : BaseViewModel<I, VS>, I: MviIntent, VS: MviViewState>
    : DaggerFragment(), MviView<I, VS> {

    protected var disposables = CompositeDisposable()

    lateinit var viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribeToNavigationChanges()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    /**
     * Connect the [MviView] with the [MviViewModel]
     * We subscribe to the [MviViewModel] before passing it the [MviView]'s [MviIntent]s.
     * If we were to pass [MviIntent]s to the [MviViewModel] before listening to it,
     * emitted [MviViewState]s could be lost
     */
    private fun bind() {
        viewModel = initViewModel()
        // Subscribe to the ViewModel and call render for every emitted state
        disposables.add(viewModel.states().subscribe{ state -> render(state)})
        // Pass the UI's intents to the ViewModel
        viewModel.processIntents(intents())
    }

    /**
     * Initialize an appropriate ViewModel
     */
    abstract fun initViewModel(): VM

}