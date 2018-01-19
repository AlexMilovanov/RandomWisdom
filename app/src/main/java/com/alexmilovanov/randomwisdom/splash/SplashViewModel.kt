package com.alexmilovanov.randomwisdom.splash

import android.arch.lifecycle.ViewModel
import com.alexmilovanov.randomwisdom.mvibase.*
import com.alexmilovanov.randomwisdom.splash.AppLaunchResult.InitialQuotesResult
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
class SplashViewModel
@Inject constructor(private val actionProcessorHolder: AppLaunchActionProcessorHolder)
    : ViewModel(), MviViewModel<AppLaunchIntent, SplashViewState> {

    /**
     * Proxy subject used to keep the stream alive even after the UI gets recycled.
     * This is basically used to keep ongoing events and the last cached State alive
     * while the UI disconnects and reconnects on config changes.
     */
    private val intentsSubject: PublishSubject<AppLaunchIntent> = PublishSubject.create()
    private val statesObservable: Observable<SplashViewState> = compose()

    /**
     * take only the first ever InitialIntent and all intents of other types
     * to avoid reloading data on config changes
     */
    private val intentFilter: ObservableTransformer<AppLaunchIntent, AppLaunchIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge<AppLaunchIntent>(
                        shared.ofType(AppLaunchIntent.InitialQuotesIntent::class.java).take(1),
                        shared.notOfType(AppLaunchIntent.InitialQuotesIntent::class.java)
                )
            }
        }

    override fun processIntents(intents: Observable<AppLaunchIntent>) {
        intents.subscribe(intentsSubject)
    }

    override fun states() = statesObservable

    /**
     * Compose all components to create the stream logic
     */
    private fun compose(): Observable<SplashViewState> {
        return intentsSubject
                // Apply intent filter
                .compose<AppLaunchIntent>(intentFilter)
                .map{intent -> actionFromIntent (intent)}
                .compose<AppLaunchResult>(actionProcessorHolder.initialQuotesProcessor)
                // Cache each state and pass it to the reducer to create a new state from
                // the previous cached one and the latest Result emitted from the action processor.
                // The Scan operator is used here for the caching.
                .scan(SplashViewState.idle(), SplashViewModel.reducer)
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
    private fun actionFromIntent(intent: AppLaunchIntent): AppLaunchAction {
        return when (intent) {
            AppLaunchIntent.InitialQuotesIntent -> AppLaunchAction.RequestInitialQuotesAction
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
        private val reducer = BiFunction { previousState: SplashViewState, result: AppLaunchResult ->
            when (result) {
                is InitialQuotesResult -> when (result) {
                    InitialQuotesResult.Success -> previousState.copy(
                            dataAvailable = true,
                            loading = false,
                            error = null
                    )
                    InitialQuotesResult.InFlight -> previousState.copy(
                            false,
                            loading = true,
                            error = null
                    )
                    is InitialQuotesResult.Failure -> previousState.copy(
                            dataAvailable = false,
                            loading = false,
                            error = result.error
                    )
                }
            }
        }
    }
}