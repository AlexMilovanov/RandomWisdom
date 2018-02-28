package com.alexmilovanov.randomwisdom.ui.favorites

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alexmilovanov.randomwisdom.R
import com.alexmilovanov.randomwisdom.databinding.FragmentFavoriteQuotesBinding
import com.alexmilovanov.randomwisdom.ui.main.MainNavigator
import com.alexmilovanov.randomwisdom.mvibase.MviView
import com.alexmilovanov.randomwisdom.mvibase.MviViewModel
import com.alexmilovanov.randomwisdom.mvibase.MviViewState
import com.alexmilovanov.randomwisdom.ui.common.AutoClearedValue
import com.alexmilovanov.randomwisdom.ui.common.BaseFragment
import com.alexmilovanov.randomwisdom.util.resources.ResourceProvider
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * Main UI for the favorite quotes list
 */
class FavoriteQuotesFragment :
        BaseFragment<FavoriteQuotesViewModel, FavoriteQuotesIntent, FavoriteQuotesViewState>(),
        SwipeDismissItemTouchHelper.ItemSwipedCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navigator: MainNavigator

    @Inject
    lateinit var resProvider: ResourceProvider

    private val listAdapter by lazy {
        binding.value?.rvFavorites?.adapter as FavoriteQuotesAdapter
    }

    private lateinit var binding: AutoClearedValue<FragmentFavoriteQuotesBinding>

    private val deleteIntentPublisher = PublishSubject.create<FavoriteQuotesIntent.DeleteQuoteIntent>()

    private val restoreIntentPublisher = PublishSubject.create<FavoriteQuotesIntent.RestoreQuoteIntent>()

    private val shareIntentPublisher = PublishSubject.create<FavoriteQuotesIntent.ShareQuoteIntent>()

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

    override fun intents(): Observable<FavoriteQuotesIntent> =
            Observable.merge(initialIntent(), deleteIntent(), restoreIntent(), shareIntent())

    override fun render(state: FavoriteQuotesViewState) {
        if (!state.loading && state.error == null && state.favorites != null) {
            listAdapter.apply {
                if(itemCount == 0)
                    add(state.favorites)
                else
                    replaceAll(state.favorites)
            }
        }
    }

    override fun subscribeToNavigationChanges() {
        val fragment = this@FavoriteQuotesFragment
        viewModel.run {
            notifyQuoteRemovedCommand.observe(fragment,
                    Observer { quote ->
                        quote?.let {
                            // Display snack bar and handle action button by restoring a quote in a list
                            // and sending corresponding intent
                            disposables.add(navigator.showNotificationWithAction(resProvider.string(R.string.msg_removed_from_favs))
                                    .subscribe {
                                        restoreIntentPublisher.onNext(FavoriteQuotesIntent.RestoreQuoteIntent(quote))
                                    })
                        }
                    })

            notifyQuoteRestoredCommand.observe(fragment,
                    Observer { quote ->
                        // Insert restored quote back in the list
                        quote?.let {
                            listAdapter.add(quote)
                        }
                    })

            shareCommand.observe(fragment,
                    Observer { text ->
                        text?.let {
                            navigator.shareText(text)
                        }
                    })
        }
    }

    override fun initViewModel(): FavoriteQuotesViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(FavoriteQuotesViewModel::class.java)

    /**
     * Remove quote upon swipe gesture and send corresponding intent
     */
    override fun onSwipe(position: Int) {
        val item = listAdapter.getItem(position)
        listAdapter.remove(item)
        deleteIntentPublisher.onNext(FavoriteQuotesIntent.DeleteQuoteIntent(item))
    }

    /**
     * The Intent the [MviView] emit to convey to the [MviViewModel]
     * to remove selected quote from Favorites
     */
    private fun deleteIntent(): Observable<FavoriteQuotesIntent.DeleteQuoteIntent> = deleteIntentPublisher

    /**
     * The Intent the [MviView] emit to convey to the [MviViewModel]
     * to restore deleted quote in Favorites
     */
    private fun restoreIntent(): Observable<FavoriteQuotesIntent.RestoreQuoteIntent> = restoreIntentPublisher

    /**
     * The Intent the [MviView] emit to convey to the [MviViewModel]
     * to share selected quote
     */
    private fun shareIntent(): Observable<FavoriteQuotesIntent.ShareQuoteIntent> = shareIntentPublisher

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
        binding.value!!.rvFavorites.adapter = FavoriteQuotesAdapter(listOf())
        // Attach the RecyclerView to itemTouchHelper.SimpleCallback implementation.
        val itemSimpleCallback: ItemTouchHelper.SimpleCallback = SwipeDismissItemTouchHelper(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(itemSimpleCallback).attachToRecyclerView(binding.value!!.rvFavorites)
        disposables.add(
                listAdapter.itemClickObservable.subscribe { quote ->
                    shareIntentPublisher.onNext(FavoriteQuotesIntent.ShareQuoteIntent(quote))
                }
        )
    }

    companion object {
        operator fun invoke() = FavoriteQuotesFragment()
    }
}