package com.alexmilovanov.randomwisdom.splash

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alexmilovanov.randomwisdom.R
import com.alexmilovanov.randomwisdom.mvibase.*
import com.alexmilovanov.randomwisdom.view.BaseFragment
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_splash.*
import javax.inject.Inject

/**
 * Main UI for a splash screen
 */
class SplashFragment : BaseFragment<SplashViewModel, AppLaunchIntent, SplashViewState>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navigator: SplashNavigator

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun initViewModel() =
            ViewModelProviders.of(this, viewModelFactory).get(SplashViewModel::class.java)

    override fun intents() = initialIntent()

    override fun render(state: SplashViewState) {
        if (state.loading) tv_logo.text = "Loading"
        if (state.error != null) {
            tv_logo.text = state.error.localizedMessage
        }
        if (state.dataAvailable) {
            navigator.navigateToQuotes()
        }
    }

    /**
     * The initial Intent the [MviView] emit to convey to the [MviViewModel]
     * that it is ready to receive data.
     * This initial Intent is also used to pass any parameters the [MviViewModel] might need
     * to render the initial [MviViewState] (e.g. the task id to load).
     */
    private fun initialIntent(): Observable<AppLaunchIntent> {
        return Observable.just(AppLaunchIntent.InitialQuotesIntent)
    }
}