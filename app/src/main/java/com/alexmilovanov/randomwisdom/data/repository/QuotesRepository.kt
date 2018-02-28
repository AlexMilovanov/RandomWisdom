package com.alexmilovanov.randomwisdom.data.repository

import com.alexmilovanov.randomwisdom.data.network.ApiService
import com.alexmilovanov.randomwisdom.data.persistence.quotes.FavoritesDao
import com.alexmilovanov.randomwisdom.data.persistence.quotes.Quote
import io.reactivex.*
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject
import kotlin.NoSuchElementException

/**
 * Single source of truth for quotes related data
 */
class QuotesRepository
@Inject
constructor(private val apiService: ApiService, private val favDao: FavoritesDao) : IQuotesRepository {

    // Cached quotes serving as local data source
    private val cachedQuotes: Queue<Quote> = ArrayDeque<Quote>()

    /**
     * Ensure cached quotes are available before the first quote is requested
     */
    override fun loadInitialQuotes(): Completable {
        if (cachedQuotes.isEmpty()) {
            return refreshQuotes().toCompletable()
        }
        return Completable.complete()
    }

    /**
     * Gets the first quote from the cached queue if not empty. Request quotes refresh otherwise.
     */
    override fun getRandomQuote(): Single<Quote> {
        return getNextQuote()
                .switchIfEmpty(Maybe.just(Quote()))
                .toSingle()
                .flatMap { q ->
                    if (q.quote.isEmpty()) {
                        refreshQuotes()
                                .flatMap { _ ->
                                    getNextQuote()
                                            .defaultIfEmpty(Quote())
                                            .toSingle()
                                }
                    } else {
                        Single.just(q)
                    }
                }
    }

    /**
     * Add quote to favorites if it hasn't been added before. Remove from favorites otherwise.
     */
    override fun addOrRemoveFromFavorites(quote: Quote): Single<Boolean> {
        return favDao.hasQuote(quote.id)
                .switchIfEmpty(Maybe.just(Quote()))
                .toSingle()
                .flatMap { q ->
                    if (q.quote.isEmpty()) {
                        val quoteToAdd =
                                // if quote is being added to Favorites for the first time, set
                                // corresponding timestamp
                                if (quote.timestamp == 0L) {
                                    quote.copy(timestamp = System.currentTimeMillis())
                                } else {
                                    quote
                                }
                        favDao.addToFavorites(quoteToAdd)
                        Single.just(true)
                    } else {
                        favDao.deleteFromFavorites(quote.id)
                        Single.just(false)
                    }
                }
    }

    /**
     * Provide quote text to be shared
     */
    override fun getShareQuoteText(quote: Quote): Single<String> {
        return Single.just("\"" + quote.quote + "\" —" + quote.author)
    }

    /**
     * Retrieve a favorite quotes list if exists. Return empty list otherwise.
     */
    override fun getFavoritesQuotes(): Observable<List<Quote>> {
        return favDao.loadFavoriteQuotes()
                .defaultIfEmpty(mutableListOf())
                .toObservable()
    }

    /**
     * Retrieve another portion of quotes from the remote source and cache it locally
     */
    private fun refreshQuotes(): Single<List<Quote>> {
        return apiService.getCachedQuotes()
                .toObservable()
                .flatMap { respQuotes -> Observable.fromIterable(respQuotes) }
                .map { respQuote -> Quote(quote = respQuote.quote, author = respQuote.author) }
                .doOnNext { quote -> cachedQuotes.add(quote) }
                .toList()
    }

    /**
     * Removes and returns the head of the queue. Returns empty value if queue is empty.
     */
    private fun getNextQuote(): Maybe<Quote> {
        return try {
            Maybe.just(cachedQuotes.remove())
        } catch (e: NoSuchElementException) {
            Maybe.empty()
        }
    }

}