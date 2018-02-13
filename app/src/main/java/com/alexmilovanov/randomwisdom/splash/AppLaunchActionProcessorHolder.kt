package com.alexmilovanov.randomwisdom.splash

import com.alexmilovanov.randomwisdom.splash.AppLaunchResult.InitialQuotesResult
import com.alexmilovanov.randomwisdom.data.repository.IQuotesRepository
import com.alexmilovanov.randomwisdom.mvibase.MviAction
import com.alexmilovanov.randomwisdom.mvibase.MviResult
import com.alexmilovanov.randomwisdom.util.schedulers.ISchedulerProvider
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import javax.inject.Inject

/**
 * Contains and executes the business logic for all emitted [MviAction]
 * and returns one unique [Observable] of [MviResult].
 */
class AppLaunchActionProcessorHolder
@Inject constructor(private val quotesRepo: IQuotesRepository,
                    private val schedulerProvider: ISchedulerProvider) {

    internal val initialQuotesProcessor =
            ObservableTransformer<AppLaunchAction, AppLaunchResult> { actions ->
                actions.flatMap { _ ->
                    quotesRepo.loadInitialQuotes()
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            // Emit success result object when quotes are loaded
                            .andThen(Observable.just(InitialQuotesResult.Success))
                            // Wrap returned data into an immutable object
                            .cast(InitialQuotesResult::class.java)
                            // Wrap any error into an immutable object and pass it down the stream
                            // without crashing.
                            // Errors are data and hence, should just be part of the stream.
                            .onErrorReturn { it -> InitialQuotesResult.Failure(it) }
                            // Emit an InFlight event to notify the subscribers (e.g. the UI) we are
                            // doing work and waiting on a response.
                            .startWith(InitialQuotesResult.InFlight)
                }
            }

}