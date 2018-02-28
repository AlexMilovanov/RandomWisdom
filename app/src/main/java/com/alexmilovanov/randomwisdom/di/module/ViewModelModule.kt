package com.alexmilovanov.randomwisdom.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.alexmilovanov.randomwisdom.ui.favorites.FavoriteQuotesViewModel
import com.alexmilovanov.randomwisdom.ui.randomquote.RandomQuoteViewModel
import com.alexmilovanov.randomwisdom.ui.splash.SplashViewModel
import com.alexmilovanov.randomwisdom.ui.common.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton


/**
 * Module providing view models dependencies
 */
@Module
(includes = [ActivityModule::class])
abstract class ViewModelModule {

    @Binds
    @Singleton
    abstract fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RandomQuoteViewModel::class)
    abstract fun bindRandomQuoteViewModel(viewModel: RandomQuoteViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteQuotesViewModel::class)
    abstract fun bindFavoriteQuotesViewModel(viewModel: FavoriteQuotesViewModel): ViewModel

}