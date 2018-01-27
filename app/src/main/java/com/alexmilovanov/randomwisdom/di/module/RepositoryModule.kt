package com.alexmilovanov.randomwisdom.di.module

import com.alexmilovanov.randomwisdom.data.repository.IQuotesRepository
import com.alexmilovanov.randomwisdom.data.repository.QuotesRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * Module providing repositories related dependencies
 */
@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun provideQuotesRepository(repo: QuotesRepository): IQuotesRepository

}