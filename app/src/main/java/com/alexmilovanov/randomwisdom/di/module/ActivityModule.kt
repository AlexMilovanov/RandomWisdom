package com.alexmilovanov.randomwisdom.di.module

import com.alexmilovanov.randomwisdom.main.MainActivity
import com.alexmilovanov.randomwisdom.di.Scopes
import com.alexmilovanov.randomwisdom.main.MainActivityModule
import com.alexmilovanov.randomwisdom.splash.SplashActivity
import com.alexmilovanov.randomwisdom.splash.SplashActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Module providing activities related dependencies
 */
@Module
internal abstract class ActivityModule {

    @Scopes.PerActivity
    @ContributesAndroidInjector(modules = [SplashActivityModule::class])
    abstract fun contributeSplashActivity(): SplashActivity

    @Scopes.PerActivity
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeMainActivity(): MainActivity

}