package com.alexmilovanov.randomwisdom.di.data

import android.content.Context
import com.alexmilovanov.randomwisdom.RandomWisdomApp
import com.alexmilovanov.randomwisdom.di.ApplicationContext
import com.alexmilovanov.randomwisdom.util.resources.IResourceProvider
import com.alexmilovanov.randomwisdom.util.resources.ResourceProvider
import com.alexmilovanov.randomwisdom.util.schedulers.ISchedulerProvider
import com.alexmilovanov.randomwisdom.util.schedulers.SchedulerProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Module providing Application dependencies
 */
@Module
interface UtilModule {

    @Binds
    @Singleton
    fun provideResourceProvider(provider: SchedulerProvider): ISchedulerProvider

    @Binds
    @Singleton
    fun provideSchedulerProvider(provider: ResourceProvider): IResourceProvider

}