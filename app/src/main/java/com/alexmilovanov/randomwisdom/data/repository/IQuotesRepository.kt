package com.alexmilovanov.randomwisdom.data.repository

import com.alexmilovanov.randomwisdom.data.persistence.quotes.Quote
import io.reactivex.Single

/**
 * Defines methods to be implemented by Repo responsible for providing quotes data
 */
interface IQuotesRepository {

    fun getRandomQuote(): Single<Quote>
}