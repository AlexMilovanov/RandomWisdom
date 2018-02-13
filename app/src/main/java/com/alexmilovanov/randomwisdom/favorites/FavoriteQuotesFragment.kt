package com.alexmilovanov.randomwisdom.favorites

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alexmilovanov.randomwisdom.databinding.FragmentFavoriteQuotesBinding
import com.alexmilovanov.randomwisdom.mvibase.MviView
import com.alexmilovanov.randomwisdom.mvibase.MviViewModel
import com.alexmilovanov.randomwisdom.mvibase.MviViewState
import com.alexmilovanov.randomwisdom.uicommon.AutoClearedValue
import com.alexmilovanov.randomwisdom.uicommon.BaseFragment
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Main UI for the favorite quotes list
 */
class FavoriteQuotesFragment :
        BaseFragment<FavoriteQuotesViewModel, FavoriteQuotesIntent, FavoriteQuotesViewState>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: AutoClearedValue<FragmentFavoriteQuotesBinding>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val dataBinding = FragmentFavoriteQuotesBinding.inflate(inflater, container, false)
        binding = AutoClearedValue(this, dataBinding)

        setupListAdapter()

        return dataBinding.root
    }

    override fun intents() = initialIntent()

    override fun render(state: FavoriteQuotesViewState) {
        if(!state.loading && state.error==null && state.favorites != null) {
            (binding.value?.rvFavorites?.adapter as FavoriteQuotesAdapter).add(state.favorites)
        }
    }

    override fun subscribeToNavigationChanges() {

    }

    override fun initViewModel(): FavoriteQuotesViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(FavoriteQuotesViewModel::class.java)

    /**
     * The initial Intent the [MviView] emit to convey to the [MviViewModel]
     * that it is ready to receive data.
     * This initial Intent is also used to pass any parameters the [MviViewModel] might need
     * to render the initial [MviViewState] (e.g. the task id to load).
     */
    private fun initialIntent(): Observable<FavoriteQuotesIntent> {
        return Observable.just(FavoriteQuotesIntent.InitialIntent)
    }

    private fun setupListAdapter() {
        binding.value?.rvFavorites?.adapter = FavoriteQuotesAdapter(listOf())
    }
}