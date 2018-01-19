package com.alexmilovanov.randomwisdom.randomquote

import android.arch.lifecycle.ViewModel
import com.alexmilovanov.randomwisdom.mvibase.MviViewModel
import com.alexmilovanov.randomwisdom.mvibase.MviIntent
import com.alexmilovanov.randomwisdom.mvibase.MviAction
import com.alexmilovanov.randomwisdom.mvibase.MviViewState
import com.alexmilovanov.randomwisdom.mvibase.MviView
import com.alexmilovanov.randomwisdom.mvibase.MviResult
import com.alexmilovanov.randomwisdom.randomquote.RandomQuoteResult.RequestNextQuoteResult
import com.alexmilovanov.randomwisdom.randomquote.RandomQuoteIntent.InitialIntent
import com.alexmilovanov.randomwisdom.util.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * Listens to user actions from the UI, retrieves the data and updates the UI as required.
 *
 * @property actionProcessorHolder Contains and executes the business logic of all emitted actions.
 */
class RandomQuoteViewModel
@Inject constructor( private val actionProcessorHolder: RandomQuoteActionProcessorHolder)
    : ViewModel(), MviViewModel<RandomQuoteIntent, RandomQuoteViewState> {

    /**
     * Proxy subject used to keep the stream alive even after the UI gets recycled.
     * This is basically used to keep ongoing events and the last cached State alive
     * while the UI disconnects and reconnects on config changes.
     */
    private val intentsSubject: PublishSubject<RandomQuoteIntent> = PublishSubject.create()
    private val statesObservable: Observable<RandomQuoteViewState> = compose()

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


    override fun processIntents(intents: Observable<RandomQuoteIntent>) {
        intents.subscribe(intentsSubject)
    }

    override fun states() = statesObservable

    /**
     * Compose all components to create the stream logic
     */
    private fun compose(): Observable<RandomQuoteViewState> {
        return intentsSubject
                .compose<RandomQuoteIntent>(intentFilter)
                .map{ intent -> actionFromIntent (intent) }
                .compose<RandomQuoteResult>(actionProcessorHolder.nextQuoteProcessor)
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
    private fun actionFromIntent(intent: RandomQuoteIntent): RandomQuoteAction {
        return when (intent) {
            is RandomQuoteIntent.InitialIntent -> RandomQuoteAction.RequestNextQuoteAction
            is RandomQuoteIntent.NextQuoteIntent -> RandomQuoteAction.RequestNextQuoteAction
        }
    }

    companion object {

        /**
         * The Reducer is where [MviViewState], that the [MviView] will use to
         * render itself, are created.
         * It takes the last cached [MviViewState], the latest [MviResult] and
         * creates a new [MviViewState] by only updating the related fields.
         * This is basically like a big switch statement of all possible types for the [MviResult]
         */
        private val reducer = BiFunction { previousState: RandomQuoteViewState, result: RandomQuoteResult ->
            when (result) {
                is RequestNextQuoteResult -> when (result) {
                    is RequestNextQuoteResult.Success -> previousState.copy(
                            quoteText = result.quote.quote,
                            author = result.quote.author,
                            error = null,
                            loading = false
                    )
                    RequestNextQuoteResult.InFlight -> previousState.copy(
                            error = null,
                            loading = true
                    )
                    is RequestNextQuoteResult.Failure -> previousState.copy(error = result.error)
                }
            }
        }
    }

}