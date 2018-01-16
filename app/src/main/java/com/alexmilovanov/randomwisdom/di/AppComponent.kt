package com.alexmilovanov.randomwisdom.di

import com.alexmilovanov.randomwisdom.RandomWisdomApp
import com.alexmilovanov.randomwisdom.di.data.RepositoryModule
import com.alexmilovanov.randomwisdom.di.data.NetworkModule
import com.alexmilovanov.randomwisdom.di.data.PersistenceModule
import com.alexmilovanov.randomwisdom.di.ui.ActivityModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = [AndroidSupportInjectionModule::class,
                      AppModule::class,
                      ActivityModule::class,
                      NetworkModule::class,
                      PersistenceModule::class,
                      RepositoryModule::class])
interface AppComponent : AndroidInjector<RandomWisdomApp> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<RandomWisdomApp>() {}

}