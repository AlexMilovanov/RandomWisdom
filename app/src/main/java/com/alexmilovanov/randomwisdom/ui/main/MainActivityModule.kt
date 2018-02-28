package com.alexmilovanov.randomwisdom.ui.main

import com.alexmilovanov.randomwisdom.di.Scopes
import com.alexmilovanov.randomwisdom.ui.favorites.FavoriteQuotesFragment
import com.alexmilovanov.randomwisdom.ui.randomquote.RandomQuoteFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Module providing MainActivity related dependencies
 */
@Module
interface MainActivityModule {

    @Scopes.PerFragment
    @ContributesAndroidInjector
    fun contributeRandomQuoteFragment(): RandomQuoteFragment

    @Scopes.PerFragment
    @ContributesAndroidInjector
    fun contributeFavoriteQuotesFragment(): FavoriteQuotesFragment

    @Scopes.PerActivity
    @Binds
    fun provideMainNavigator(navigator: MainNavigationController): MainNavigator

}