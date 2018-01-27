package com.alexmilovanov.randomwisdom.di.module

import android.content.Context
import com.alexmilovanov.randomwisdom.RandomWisdomApp
import com.alexmilovanov.randomwisdom.di.ApplicationContext
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

}