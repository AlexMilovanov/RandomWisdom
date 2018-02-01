package com.alexmilovanov.randomwisdom.randomquote

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alexmilovanov.randomwisdom.R
import com.alexmilovanov.randomwisdom.view.BaseFragment
import com.alexmilovanov.randomwisdom.mvibase.*
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_random_quote.*
import kotlinx.android.synthetic.main.fragment_splash.*
import javax.inject.Inject


/**
 * Main UI for the random quote screen.
 */
class RandomQuoteFragment : BaseFragment<RandomQuoteViewModel, RandomQuoteIntent, RandomQuoteViewState>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_random_quote, container, false)
    }

    override fun initViewModel() =
            ViewModelProviders.of(this, viewModelFactory).get(RandomQuoteViewModel::class.java)

    override fun intents(): Observable<RandomQuoteIntent> =
            Observable.merge(initialIntent(), nextQuoteIntent())

    override fun render(state: RandomQuoteViewState) {
        if (state.loading) tv_quote.text = "Loading"
        if (state.error != null) {
            tv_quote.text = state.error.localizedMessage
        }
        if (!state.author.isEmpty()) {
            tv_author.text = state.author
        }
        if(!state.quoteText.isEmpty()) {
            tv_quote.text = state.quoteText
        }
    }

    /**
     * The initial Intent the [MviView] emit to convey to the [MviViewModel]
     * that it is ready to receive data.
     * This initial Intent is also used to pass any parameters the [MviViewModel] might need
     * to render the initial [MviViewState].
     */
    private fun initialIntent(): Observable<RandomQuoteIntent.InitialIntent> =
            Observable.just(RandomQuoteIntent.InitialIntent)

    /**
     * The Intent the [MviView] emit to convey to the [MviViewModel]
     * to request another random quote.
     */
    private fun nextQuoteIntent(): Observable<RandomQuoteIntent.NextQuoteIntent> =
            RxView.clicks(btn_next).map {
                RandomQuoteIntent.NextQuoteIntent
            }

}