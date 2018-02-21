package com.alexmilovanov.randomwisdom.randomquote

import com.alexmilovanov.randomwisdom.data.repository.IQuotesRepository
import io.reactivex.Observable
import com.alexmilovanov.randomwisdom.mvibase.MviAction
import com.alexmilovanov.randomwisdom.mvibase.MviResult
import com.alexmilovanov.randomwisdom.util.schedulers.ISchedulerProvider
import com.alexmilovanov.randomwisdom.randomquote.RandomQuoteResult.RequestNextQuoteResult
import com.alexmilovanov.randomwisdom.randomquote.RandomQuoteAction.RequestNextQuoteAction
import com.alexmilovanov.randomwisdom.randomquote.RandomQuoteResult.LikeQuoteResult
import com.alexmilovanov.randomwisdom.randomquote.RandomQuoteAction.LikeQuoteAction
import com.alexmilovanov.randomwisdom.randomquote.RandomQuoteAction.ShareQuoteAction
import com.alexmilovanov.randomwisdom.randomquote.RandomQuoteResult.ShareQuoteResult
import io.reactivex.ObservableTransformer
import javax.inject.Inject

/**
 * Contains and executes the business logic for all emitted [MviAction]
 * and returns one unique [Observable] of [MviResult].
 */
class RandomQuoteActionProcessorHolder
@Inject constructor(private val quotesRepo: IQuotesRepository,
                    private val schedulerProvider: ISchedulerProvider) {

    /**
     * Splits the [Observable] to match each type of [MviAction] to
     * its corresponding business logic processor. Each processor takes a defined [MviAction],
     * returns a defined [MviResult]
     * The global actionProcessor then merges all [Observable] back to
     * one unique [Observable].
     *
     * The splitting is done using [Observable.publish] which allows almost anything
     * on the passed [Observable] as long as one and only one [Observable] is returned.
     *
     * An security layer is also added for unhandled [MviAction] to allow early crash
     * at runtime to easy the maintenance.
     */
    internal var actionProcessor =
            ObservableTransformer<RandomQuoteAction, RandomQuoteResult> { actions ->
                actions.publish({ shared ->
                    Observable.merge<RandomQuoteResult>(
                            // Match RequestNextQuote to nextQuoteProcessor
                            shared.ofType(RequestNextQuoteAction::class.java).compose(nextQuoteProcessor),
                            // Match LikeQuote to likeQuoteProcessor
                            shared.ofType(LikeQuoteAction::class.java).compose(likeQuoteProcessor),
                            // Match ShareQuote to ShareQuoteProcessor
                            shared.ofType(ShareQuoteAction::class.java).compose(shareQuoteProcessor))
                            .mergeWith(
                                    // Error for not implemented actions
                                    shared.filter({ v ->
                                        (v != RequestNextQuoteAction &&
                                                v !is LikeQuoteAction &&
                                                v !is ShareQuoteAction)
                                    })
                                            .flatMap({ w ->
                                                Observable.error<RandomQuoteResult>(
                                                        IllegalArgumentException("Unknown Action type: " + w))
                                            }))
                })
            }

    private val nextQuoteProcessor =
            ObservableTransformer<RequestNextQuoteAction, RequestNextQuoteResult> { actions ->
                actions.flatMap { _ ->
                    quotesRepo.getRandomQuote()
                            // Transform the Single to an Observable to allow emission of multiple
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

    private val likeQuoteProcessor =
            ObservableTransformer<LikeQuoteAction, LikeQuoteResult> { actions ->
                actions.flatMap { action ->
                    quotesRepo.addOrRemoveFromFavorites(action.quote)
                            // Transform the Completable to an Observable to allow emission of multiple
                            // events down the stream (e.g. the InFlight event)
                            .toObservable()
                            // Wrap returned data into an immutable object
                            .map { it -> LikeQuoteResult.Success(it) }
                            .cast(LikeQuoteResult::class.java)
                            // Wrap any error into an immutable object and pass it down the stream
                            // without crashing.
                            // Because errors are data and hence, should just be part of the stream.
                            .onErrorReturn { it -> LikeQuoteResult.Failure(it) }
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                }
            }

    private val shareQuoteProcessor =
            ObservableTransformer<ShareQuoteAction, ShareQuoteResult> { actions ->
                actions.flatMap { action ->
                    quotesRepo.getShareQuoteText(action.quote)
                            // Transform the Completable to an Observable to allow emission of multiple
                            // events down the stream (e.g. the InFlight event)
                            .toObservable()
                            // Wrap returned data into an immutable object
                            .map { it -> ShareQuoteResult.Success(it) }
                            .cast(ShareQuoteResult::class.java)
                            // Wrap any error into an immutable object and pass it down the stream
                            // without crashing.
                            // Because errors are data and hence, should just be part of the stream.
                            .onErrorReturn { it -> ShareQuoteResult.Failure(it) }
                            .subscribeOn(schedulerProvider.computation())
                            .observeOn(schedulerProvider.ui())
                }
            }

}