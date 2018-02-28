package com.alexmilovanov.randomwisdom.ui.randomquote

import com.alexmilovanov.randomwisdom.action.QuotesAction
import com.alexmilovanov.randomwisdom.mvibase.MviIntent
import com.alexmilovanov.randomwisdom.mvibase.MviAction
import com.alexmilovanov.randomwisdom.mvibase.MviViewState
import com.alexmilovanov.randomwisdom.mvibase.MviView
import com.alexmilovanov.randomwisdom.mvibase.MviResult
import com.alexmilovanov.randomwisdom.action.QuotesAction.*
import com.alexmilovanov.randomwisdom.processor.QuotesActionProcessorHolder
import com.alexmilovanov.randomwisdom.result.QuotesResult
import com.alexmilovanov.randomwisdom.result.QuotesResult.*
import com.alexmilovanov.randomwisdom.ui.randomquote.RandomQuoteIntent.InitialIntent
import com.alexmilovanov.randomwisdom.util.ext.notOfType
import com.alexmilovanov.randomwisdom.ui.common.BaseViewModel
import com.alexmilovanov.randomwisdom.ui.common.SingleLiveEvent
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import javax.inject.Inject

/**
 * Listens to user actions from the UI, retrieves the data and updates the UI as required.
 *
 * @property actionProcessorHolder Contains and executes the business logic of all emitted actions.
 */
class RandomQuoteViewModel
@Inject constructor(private val actionProcessorHolder: QuotesActionProcessorHolder)
    : BaseViewModel<RandomQuoteIntent, RandomQuoteViewState>() {

    // Command called for the View to share some text
    val shareCommand = SingleLiveEvent<String>()

    /**
     * take only the first ever InitialIntent and all intents of other types
     * to avoid reloading data on config changes
     */
    private val intentFilter: ObservableTransformer<RandomQuoteIntent, RandomQuoteIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge<RandomQuoteIntent>(
                        shared.ofType(InitialIntent::class.java).take(1),
                        shared.notOfType(InitialIntent::class.java)
                )
            }
        }

    /**
     * Compose all components to create the stream logic
     */
    override fun compose(): Observable<RandomQuoteViewState> {
        return intentsSubject
                .compose<RandomQuoteIntent>(intentFilter)
                .map { intent -> actionFromIntent(intent) }
                .compose(actionProcessorHolder.actionProcessor)
                // Cache each state and pass it to the reducer to create a new state from
                // the previous cached one and the latest Result emitted from the action processor.
                // The Scan operator is used here for the caching.
                .scan(RandomQuoteViewState.idle(), reducer)
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
    private fun actionFromIntent(intent: RandomQuoteIntent): QuotesAction {
        return when (intent) {
            RandomQuoteIntent.InitialIntent,
            RandomQuoteIntent.NextQuoteIntent,
            RandomQuoteIntent.RetryIntent -> RequestNextQuoteAction
            is RandomQuoteIntent.LikeCurrentQuoteIntent -> {
                if(intent.quote.isLiked) {
                   RemoveFromFavoritesAction(intent.quote)
                } else {
                   AddToFavoritesAction(intent.quote)
                }
            }
            is RandomQuoteIntent.ShareCurrentQuoteIntent -> ShareQuoteAction(intent.quote)
        }
    }


    /**
     * The Reducer is where [MviViewState], that the [MviView] will use to
     * render itself, are created.
     * It takes the last cached [MviViewState], the latest [MviResult] and
     * creates a new [MviViewState] by only updating the related fields.
     * This is basically like a big switch statement of all possible types for the [MviResult]
     */
    private val reducer = BiFunction { previousState: RandomQuoteViewState, result: QuotesResult ->
        when (result) {
            is RequestNextQuoteResult -> when (result) {
                is RequestNextQuoteResult.Success ->
                    previousState.copy(
                            quote = result.quote,
                            error = null,
                            loading = false
                    )
                RequestNextQuoteResult.InFlight ->
                    previousState.copy(
                            quote = null,
                            error = null,
                            loading = true
                    )
                is RequestNextQuoteResult.Failure -> previousState.copy(
                        quote = null,
                        error = result.error,
                        loading = false
                )
            }
            is RemoveFromFavoritesResult -> when (result) {
                is RemoveFromFavoritesResult.Success -> {
                    // notify user on successful removal from Favorites
                    previousState.copy(quote = result.quote)
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
                    previousState.copy(quote = result.quote)
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