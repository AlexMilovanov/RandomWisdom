package com.alexmilovanov.randomwisdom.randomquote

import com.alexmilovanov.randomwisdom.data.repository.IQuotesRepository
import io.reactivex.Observable
import com.alexmilovanov.randomwisdom.mvibase.MviAction
import com.alexmilovanov.randomwisdom.mvibase.MviResult
import com.alexmilovanov.randomwisdom.util.schedulers.ISchedulerProvider
import com.alexmilovanov.randomwisdom.randomquote.RandomQuoteAction.RequestNextQuoteAction
import com.alexmilovanov.randomwisdom.randomquote.RandomQuoteResult.RequestNextQuoteResult
import io.reactivex.ObservableTransformer
import javax.inject.Inject

/**
 * Contains and executes the business logic for all emitted [MviAction]
 * and returns one unique [Observable] of [MviResult].
 */
class RandomQuoteActionProcessorHolder
@Inject constructor(private val quotesRepo: IQuotesRepository,
                    private val schedulerProvider: ISchedulerProvider) {

    internal val nextQuoteProcessor =
            ObservableTransformer<RandomQuoteAction, RandomQuoteResult> { action ->
                action.flatMap { _ ->
                    quotesRepo.getRandomQuote()
                            // Transform the Completable to an Observable to allow emission of multiple
                            // events down the stream (e.g. the InFlight event)
                            .toObservable()
                            // Wrap returned data into an immutable object
                            .map { it -> RequestNextQuoteResult.Success(it) }
                            .cast(RequestNextQuoteResult::class.java)
                            // Wrap any error into an immutable object and pass it down the stream
                            // without crashing.
                            // Because errors are data and hence, should just be part of the stream.
                            .onErrorReturn { it -> RequestNextQuoteResult.Failure(it) }
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            // Emit an InFlight event to notify the subscribers (e.g. the UI) we are
                            // doing work and waiting on a response.
                            .startWith(RequestNextQuoteResult.InFlight)
                }
            }
}