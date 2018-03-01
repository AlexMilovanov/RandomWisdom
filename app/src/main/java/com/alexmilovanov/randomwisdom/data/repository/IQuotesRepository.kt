package com.alexmilovanov.randomwisdom.data.repository

import com.alexmilovanov.randomwisdom.data.persistence.quotes.Quote
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Defines methods to be implemented by Repo responsible for providing quotes data
 */
interface IQuotesRepository {

    fun loadInitialQuotes(): Completable
    fun getRandomQuote(): Single<Quote>
    fun addOrRemoveFromFavorites(quote: Quote): Single<Boolean>
    fun getShareQuoteText(quote: Quote): Single<String>
    fun getFavoritesQuotes(query: String): Observable<List<Quote>>
}