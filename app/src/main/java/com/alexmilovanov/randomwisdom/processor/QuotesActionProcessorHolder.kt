package com.alexmilovanov.randomwisdom.processor

import com.alexmilovanov.randomwisdom.action.QuotesAction
import com.alexmilovanov.randomwisdom.data.repository.IQuotesRepository
import com.alexmilovanov.randomwisdom.mvibase.MviAction
import com.alexmilovanov.randomwisdom.mvibase.MviResult
import com.alexmilovanov.randomwisdom.result.QuotesResult.*
import com.alexmilovanov.randomwisdom.action.QuotesAction.*
import com.alexmilovanov.randomwisdom.result.QuotesResult
import com.alexmilovanov.randomwisdom.util.schedulers.ISchedulerProvider
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import javax.inject.Inject


/**
 * Contains and executes the business logic for all emitted [MviAction]
 * and returns one unique [Observable] of [MviResult].
 */
class QuotesActionProcessorHolder
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
            ObservableTransformer<QuotesAction, QuotesResult> { actions ->
                actions.publish({ shared ->
                    Observable.merge<QuotesResult>(listOf(
                            // Match Actions to appropriate Processors
                            shared.ofType(RequestInitialQuotesAction::class.java).compose(initialQuotesProcessor),
                            shared.ofType(RequestNextQuoteAction::class.java).compose(nextQuoteProcessor),
                            shared.ofType(ShareQuoteAction::class.java).compose(shareQuoteProcessor),
                            shared.ofType(AddToFavoritesAction::class.java).compose(addQuoteProcessor),
                            shared.ofType(RemoveFromFavoritesAction::class.java).compose(removeQuoteProcessor),
                            shared.ofType(RequestFavoritesAction::class.java).compose(favoriteQuotesProcessor)))
                            .mergeWith(
                                    // Error for not implemented actions
                                    shared.filter({ v ->
                                        (v != RequestInitialQuotesAction &&
                                                v !is RequestNextQuoteAction &&
                                                v !is ShareQuoteAction &&
                                                v !is AddToFavoritesAction &&
                                                v !is RemoveFromFavoritesAction &&
                                                v !is RequestFavoritesAction)
                                    })
                                            .flatMap({ w ->
                                                Observable.error<QuotesResult>(
                                                        IllegalArgumentException("Unknown Action type: " + w))
                                            }))
                })
            }

    private val initialQuotesProcessor =
            ObservableTransformer<RequestInitialQuotesAction, InitialQuotesResult> { actions ->
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

    private val favoriteQuotesProcessor =
            ObservableTransformer<RequestFavoritesAction, RequestFavoritesResult> { actions ->
                actions.flatMap { _ ->
                    quotesRepo.getFavoritesQuotes()
                            // Wrap returned data into an immutable object
                            .map { it -> RequestFavoritesResult.Success(it) }
                            .cast(RequestFavoritesResult::class.java)
                            // Wrap any error into an immutable object and pass it down the stream
                            // without crashing.
                            // Errors are data and hence, should just be part of the stream.
                            .onErrorReturn { it -> RequestFavoritesResult.Failure(it) }
                            // Wrap returned data into an immutable object
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            // Emit an InFlight event to notify the subscribers (e.g. the UI) we are
                            // doing work and waiting on a response.
                            .startWith(RequestFavoritesResult.InFlight)
                }
            }

    private val removeQuoteProcessor =
            ObservableTransformer<RemoveFromFavoritesAction, RemoveFromFavoritesResult> { actions ->
                actions.flatMap { action ->
                    quotesRepo.addOrRemoveFromFavorites(action.quote)
                            // Transform the Completable to an Observable to allow emission of multiple
                            // events down the stream
                            .toObservable()
                            // Wrap returned data into an immutable object
                            .map { _ -> RemoveFromFavoritesResult.Success(action.quote.copy(isLiked = false)) }
                            .cast(RemoveFromFavoritesResult::class.java)
                            // Wrap any error into an immutable object and pass it down the stream
                            // without crashing.
                            // Because errors are data and hence, should just be part of the stream.
                            .onErrorReturn { it -> RemoveFromFavoritesResult.Failure(it) }
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                }
            }

    private val addQuoteProcessor =
            ObservableTransformer<AddToFavoritesAction, AddToFavoritesResult> { actions ->
                actions.flatMap { action ->
                    quotesRepo.addOrRemoveFromFavorites(action.quote)
                            // Transform the Completable to an Observable to allow emission of multiple
                            // events down the stream
                            .toObservable()
                            // Wrap returned data into an immutable object
                            .map { _ -> AddToFavoritesResult.Success(action.quote.copy(isLiked = true)) }
                            .cast(AddToFavoritesResult::class.java)
                            // Wrap any error into an immutable object and pass it down the stream
                            // without crashing.
                            // Because errors are data and hence, should just be part of the stream.
                            .onErrorReturn { it -> AddToFavoritesResult.Failure(it) }
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                }
            }

}