package com.alexmilovanov.randomwisdom.data.repository

import com.alexmilovanov.randomwisdom.data.network.ApiService
import com.alexmilovanov.randomwisdom.data.persistence.quotes.Quote
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

/**
 * Single source of truth for quotes related data
 */
class QuotesRepository
@Inject
constructor(private val apiService: ApiService) : IQuotesRepository {

    // Cached quotes serving as data local source
    private val cachedQuotes: Queue<Quote> = PriorityQueue()

    /**
     * Gets first quote from the cached queue if not empty. Request quotes refresh otherwise.
     */
    override fun getRandomQuote(): Single<Quote> {

        var quote = getNextQuote()

        if(quote == null) {
            refreshQuotes()
               .andThen { quote = getNextQuote() }
        }

        return Single.just(quote)
    }

    /**
     * Retrieve another portion of quotes from the remote source and cache it locally
     */
    private fun refreshQuotes(): Completable {
         apiService.getCachedQuotes()
                .toObservable()
                .flatMap { respQuotes -> Observable.fromIterable(respQuotes) }
                .map { respQuote -> Quote(respQuote.quote, respQuote.author) }
                .doOnNext { quote -> cachedQuotes.add(quote) }

        return Completable.complete()
    }

    /**
     * Removes and returns the head of the queue. Returns null if queue is empty.
     */
    private fun getNextQuote() = cachedQuotes.poll()

}