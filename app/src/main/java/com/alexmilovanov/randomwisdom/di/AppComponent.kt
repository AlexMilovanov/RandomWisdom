package com.alexmilovanov.randomwisdom.di

import com.alexmilovanov.randomwisdom.RandomWisdomApp
import com.alexmilovanov.randomwisdom.di.module.*
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = [AndroidSupportInjectionModule::class,
                      AppModule::class,
                      NetworkModule::class,
                      PersistenceModule::class,
                      RepositoryModule::class,
                      UtilModule::class,
                      ViewModelModule::class])
interface AppComponent : AndroidInjector<RandomWisdomApp> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<RandomWisdomApp>() {}

}