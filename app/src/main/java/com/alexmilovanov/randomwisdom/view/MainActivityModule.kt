package com.alexmilovanov.randomwisdom.view

import com.alexmilovanov.randomwisdom.di.Scopes
import com.alexmilovanov.randomwisdom.randomquote.RandomQuoteFragment
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

}