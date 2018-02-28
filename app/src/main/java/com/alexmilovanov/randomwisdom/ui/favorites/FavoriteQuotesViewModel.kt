package com.alexmilovanov.randomwisdom.ui.favorites

import com.alexmilovanov.randomwisdom.action.QuotesAction
import com.alexmilovanov.randomwisdom.data.persistence.quotes.Quote
import com.alexmilovanov.randomwisdom.mvibase.*
import com.alexmilovanov.randomwisdom.ui.favorites.FavoriteQuotesIntent.*
import com.alexmilovanov.randomwisdom.action.QuotesAction.*
import com.alexmilovanov.randomwisdom.processor.QuotesActionProcessorHolder
import com.alexmilovanov.randomwisdom.result.QuotesResult
import com.alexmilovanov.randomwisdom.result.QuotesResult.*
import com.alexmilovanov.randomwisdom.ui.common.BaseViewModel
import com.alexmilovanov.randomwisdom.ui.common.SingleLiveEvent
import com.alexmilovanov.randomwisdom.util.ext.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import javax.inject.Inject

/**
 * Listens to user actions from the UI, retrieves the data and updates the UI as required.
 *
 * @property actionProcessorHolder Contains and executes the business logic of all emitted actions.
 */
class FavoriteQuotesViewModel
@Inject constructor(private val actionProcessorHolder: QuotesActionProcessorHolder)
    : BaseViewModel<FavoriteQuotesIntent, FavoriteQuotesViewState>()  {

    // Command called for the View to notify user about successful quot removal from Favorites
    val notifyQuoteRemovedCommand = SingleLiveEvent<Quote>()
    val notifyQuoteRestoredCommand = SingleLiveEvent<Quote>()

    // Command called for the View to share some text
    val shareCommand = SingleLiveEvent<String>()

    /**
     * take only the first ever InitialIntent and all intents of other types
     * to avoid reloading data on config changes
     */
    private val intentFilter: ObservableTransformer<FavoriteQuotesIntent, FavoriteQuotesIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge<FavoriteQuotesIntent>(
                        shared.ofType(FavoriteQuotesIntent.InitialIntent::class.java).take(1),
                        shared.notOfType(FavoriteQuotesIntent.InitialIntent::class.java)
                )
            }
        }

    /**
     * Compose all components to create the stream logic
     */
    override fun compose(): Observable<FavoriteQuotesViewState> {
        return intentsSubject
                .compose<FavoriteQuotesIntent>(intentFilter)
                .map { intent -> actionFromIntent(intent) }
                .compose(actionProcessorHolder.actionProcessor)
                // Cache each state and pass it to the reducer to create a new state from
                // the previous cached one and the latest Result emitted from the action processor.
                // The Scan operator is used here for the caching.
                .scan(FavoriteQuotesViewState.idle(), reducer)
                // Emit the last one event of the stream on subscription
                // Useful when a View rebinds to the ViewModel after rotation.
                .replay(1)
                // Create the stream on creation without waiting for anyone to subscribe
                // This allows the stream to stay alive even when the UI disconnects and
                // match the stream's lifecycle to the ViewModel's one.
                .autoConnect(0)
    }

    /**
     * Translate an [MviIntent] to an [MviAction].
     * Used to decouple the UI and the business logic to allow easy testings and reusability.
     */
    private fun actionFromIntent(intent: FavoriteQuotesIntent): QuotesAction {
        return when (intent) {
            InitialIntent -> QuotesAction.RequestFavoritesAction
            is DeleteQuoteIntent -> QuotesAction.RemoveFromFavoritesAction(intent.quote)
            is RestoreQuoteIntent -> AddToFavoritesAction(intent.quote)
            is ShareQuoteIntent -> ShareQuoteAction(intent.quote)
        }
    }

    /**
     * The Reducer is where [MviViewState], that the [MviView] will use to
     * render itself, are created.
     * It takes the last cached [MviViewState], the latest [MviResult] and
     * creates a new [MviViewState] by only updating the related fields.
     * This is basically like a big switch statement of all possible types for the [MviResult]
     */
    private val reducer = BiFunction { previousState: FavoriteQuotesViewState, result: QuotesResult ->
        when (result) {
            is RequestFavoritesResult -> when (result) {
                is RequestFavoritesResult.Success ->
                    previousState.copy(
                            favorites = result.quotes,
                            loading = false,
                            error = null

                    )
                RequestFavoritesResult.InFlight -> {
                    previousState.copy(
                            loading = true,
                            error = null
                    )
                }
                is RequestFavoritesResult.Failure -> {
                    previousState.copy(
                            loading = false,
                            error = result.error
                    )
                }
            }

            is RemoveFromFavoritesResult -> when (result) {
                is RemoveFromFavoritesResult.Success -> {
                    // notify user on successful quote removal from Favorites
                    notifyQuoteRemovedCommand.value = result.quote
                    previousState
                }
                is RemoveFromFavoritesResult.Failure -> {
                    previousState.copy(
                            loading = false,
                            error = result.error
                    )
                }
            }

            is AddToFavoritesResult -> when (result) {
                is AddToFavoritesResult.Success -> {
                    // notify user quote has been restored in Favorites
                    notifyQuoteRestoredCommand.value = result.quote
                    previousState
                }
                is AddToFavoritesResult.Failure -> {
                    previousState.copy(
                            loading = false,
                            error = result.error
                    )
                }
            }

            is ShareQuoteResult -> when (result) {
                is ShareQuoteResult.Success -> {
                    shareCommand.value = result.text
                    previousState
                }
                is ShareQuoteResult.Failure -> previousState.copy(error = result.error)
            }
            else -> {
                previousState
            }
        }
    }

}