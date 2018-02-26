package com.alexmilovanov.randomwisdom.favorites

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
class FavoriteQuotesActionProcessorHolder
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
            ObservableTransformer<FavoriteQuotesAction, FavoriteQuotesResult> { actions ->
                actions.publish({ shared ->
                    Observable.merge<FavoriteQuotesResult>(
                            // Match Actions to appropriate Processors
                            shared.ofType(FavoriteQuotesAction.RequestFavoritesAction::class.java).compose(initialQuotesProcessor),
                            shared.ofType(FavoriteQuotesAction.RestoreInFavoritesAction::class.java).compose(addQuoteProcessor),
                            shared.ofType(FavoriteQuotesAction.RemoveFromFavoritesAction::class.java).compose(removeQuoteProcessor),
                            shared.ofType(FavoriteQuotesAction.ShareQuoteAction::class.java).compose(shareQuoteProcessor))
                                    .mergeWith(
                                            // Error for not implemented actions
                                            shared.filter({ v ->
                                                (v != FavoriteQuotesAction.RequestFavoritesAction &&
                                                        v !is FavoriteQuotesAction.RemoveFromFavoritesAction &&
                                                        v !is FavoriteQuotesAction.RestoreInFavoritesAction &&
                                                        v !is FavoriteQuotesAction.ShareQuoteAction)
                                            })
                                                    .flatMap({ w ->
                                                        Observable.error<FavoriteQuotesResult>(
                                                                IllegalArgumentException("Unknown Action type: " + w))
                                                    }))
                })
            }

    private val initialQuotesProcessor =
            ObservableTransformer<FavoriteQuotesAction, FavoriteQuotesResult.RequestFavoritesResult> { actions ->
                actions.flatMap { _ ->
                    quotesRepo.getFavoritesQuotes()
                            // Wrap returned data into an immutable object
                            .map { it -> FavoriteQuotesResult.RequestFavoritesResult.Success(it) }
                            .cast(FavoriteQuotesResult.RequestFavoritesResult::class.java)
                            // Wrap any error into an immutable object and pass it down the stream
                            // without crashing.
                            // Errors are data and hence, should just be part of the stream.
                            .onErrorReturn { it -> FavoriteQuotesResult.RequestFavoritesResult.Failure(it) }
                            // Wrap returned data into an immutable object
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            // Emit an InFlight event to notify the subscribers (e.g. the UI) we are
                            // doing work and waiting on a response.
                            .startWith(FavoriteQuotesResult.RequestFavoritesResult.InFlight)
                }
            }

    private val removeQuoteProcessor =
            ObservableTransformer<FavoriteQuotesAction.RemoveFromFavoritesAction, FavoriteQuotesResult.RemoveFromFavoritesResult> { actions ->
                actions.flatMap { action ->
                    quotesRepo.addOrRemoveFromFavorites(action.quote)
                            // Transform the Completable to an Observable to allow emission of multiple
                            // events down the stream
                            .toObservable()
                            // Wrap returned data into an immutable object
                            .map { _ -> FavoriteQuotesResult.RemoveFromFavoritesResult.Success(action.quote) }
                            .cast(FavoriteQuotesResult.RemoveFromFavoritesResult::class.java)
                            // Wrap any error into an immutable object and pass it down the stream
                            // without crashing.
                            // Because errors are data and hence, should just be part of the stream.
                            .onErrorReturn { it -> FavoriteQuotesResult.RemoveFromFavoritesResult.Failure(it) }
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                }
            }

    private val addQuoteProcessor =
            ObservableTransformer<FavoriteQuotesAction.RestoreInFavoritesAction, FavoriteQuotesResult.AddToFavoritesResult> { actions ->
                actions.flatMap { action ->
                    quotesRepo.addOrRemoveFromFavorites(action.quote)
                            // Transform the Completable to an Observable to allow emission of multiple
                            // events down the stream
                            .toObservable()
                            // Wrap returned data into an immutable object
                            .map { _ -> FavoriteQuotesResult.AddToFavoritesResult.Success(action.quote) }
                            .cast(FavoriteQuotesResult.AddToFavoritesResult::class.java)
                            // Wrap any error into an immutable object and pass it down the stream
                            // without crashing.
                            // Because errors are data and hence, should just be part of the stream.
                            .onErrorReturn { it -> FavoriteQuotesResult.AddToFavoritesResult.Failure(it) }
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                }
            }

    private val shareQuoteProcessor =
            ObservableTransformer<FavoriteQuotesAction.ShareQuoteAction, FavoriteQuotesResult.ShareQuoteResult> { actions ->
                actions.flatMap { action ->
                    quotesRepo.getShareQuoteText(action.quote)
                            // Transform the Completable to an Observable to allow emission of multiple
                            // events down the stream (e.g. the InFlight event)
                            .toObservable()
                            // Wrap returned data into an immutable object
                            .map { it -> FavoriteQuotesResult.ShareQuoteResult.Success(it) }
                            .cast(FavoriteQuotesResult.ShareQuoteResult::class.java)
                            // Wrap any error into an immutable object and pass it down the stream
                            // without crashing.
                            // Because errors are data and hence, should just be part of the stream.
                            .onErrorReturn { it -> FavoriteQuotesResult.ShareQuoteResult.Failure(it) }
                            .subscribeOn(schedulerProvider.computation())
                            .observeOn(schedulerProvider.ui())
                }
            }
}