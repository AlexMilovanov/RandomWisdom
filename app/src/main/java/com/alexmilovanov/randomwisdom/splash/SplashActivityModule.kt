package com.alexmilovanov.randomwisdom.splash

import com.alexmilovanov.randomwisdom.di.Scopes
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Module providing SplashActivity related dependencies
 */
@Module
interface SplashActivityModule {

    @Scopes.PerFragment
    @ContributesAndroidInjector
    fun contributeSplashFragment(): SplashFragment

    @Scopes.PerActivity
    @Binds
    fun provideSplashNavigator(navigator: SplashNavigationController): SplashNavigator

}