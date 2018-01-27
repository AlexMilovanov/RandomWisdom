package com.alexmilovanov.randomwisdom.main

import com.alexmilovanov.randomwisdom.di.Scopes
import com.alexmilovanov.randomwisdom.randomquote.RandomQuoteFragment
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
    fun contributeSymptomsFragment(): RandomQuoteFragment

    @Scopes.PerActivity
    @Binds
    fun provideMainNavigator(navigator: MainNavigationController): MainNavigator

}