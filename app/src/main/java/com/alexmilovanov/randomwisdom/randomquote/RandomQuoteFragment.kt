package com.alexmilovanov.randomwisdom.randomquote

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alexmilovanov.randomwisdom.R
import com.alexmilovanov.randomwisdom.databinding.FragmentRandomQuoteBinding
import com.alexmilovanov.randomwisdom.main.MainNavigator
import com.alexmilovanov.randomwisdom.uicommon.BaseFragment
import com.alexmilovanov.randomwisdom.mvibase.*
import com.alexmilovanov.randomwisdom.uicommon.AutoClearedValue
import com.alexmilovanov.randomwisdom.util.resources.ResourceProvider
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_random_quote.*
import javax.inject.Inject


/**
 * Main UI for the random quote screen.
 */
class RandomQuoteFragment : BaseFragment<RandomQuoteViewModel, RandomQuoteIntent, RandomQuoteViewState>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var resProvider: ResourceProvider

    @Inject
    lateinit var navigator: MainNavigator

    private lateinit var binding: AutoClearedValue<FragmentRandomQuoteBinding>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val dataBinding = FragmentRandomQuoteBinding.inflate(inflater, container, false)
        binding = AutoClearedValue(this, dataBinding)

        return dataBinding.root
    }

    override fun onStart() {
        super.onStart()
        btn_favorites.setOnClickListener{ navigator.navigateToFavorites() }
    }

    override fun initViewModel() =
            ViewModelProviders.of(this, viewModelFactory).get(RandomQuoteViewModel::class.java)

    override fun intents(): Observable<RandomQuoteIntent> =
            Observable.merge(initialIntent(), nextQuoteIntent(), likeQuoteIntent(), shareQuoteIntent())

    override fun render(state: RandomQuoteViewState) {
        if (state.loading) tv_quote.text = "Loading"

        state.error?.let { tv_quote.text = state.error.localizedMessage }

        state.quote?.let { binding.value?.quote = state.quote }

        ib_like.setImageDrawable(
                resProvider.getDrawable(
                        if (state.isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border)
        )
    }

    override fun subscribeToNavigationChanges() {
        val fragment = this@RandomQuoteFragment
        viewModel.run {
            shareCommand.observe(fragment,
                    Observer {text ->
                        text?.let {
                            navigator.shareText(text)
                        }
                    })
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

    /**
     * The Intent the [MviView] emit to convey to the [MviViewModel]
     * to add or remove quote from Favorites.
     */
    private fun likeQuoteIntent(): Observable<RandomQuoteIntent> =
            RxView.clicks(ib_like).map {
                RandomQuoteIntent.LikeCurrentQuoteIntent(binding.value?.quote!!)
            }

    /**
     * The Intent the [MviView] emit to convey to the [MviViewModel]
     * to share selected quote.
     */
    private fun shareQuoteIntent(): Observable<RandomQuoteIntent> =
            RxView.clicks(ib_share).map {
                RandomQuoteIntent.ShareCurrentQuoteIntent(binding.value?.quote!!)
            }

}