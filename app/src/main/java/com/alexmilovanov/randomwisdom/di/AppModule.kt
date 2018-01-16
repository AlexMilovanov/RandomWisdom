package com.alexmilovanov.randomwisdom.di

import android.content.Context
import com.alexmilovanov.randomwisdom.RandomWisdomApp
import com.alexmilovanov.randomwisdom.view.util.ResourceProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Module providing Application dependencies
 */
@Module
class AppModule {

    @Provides
    @Singleton
    @ApplicationContext
    fun provideContext(app: RandomWisdomApp): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideResourceProvider(ctx: Context): ResourceProvider = ResourceProvider(ctx)

}