package com.alexmilovanov.randomwisdom.di.ui

import com.alexmilovanov.randomwisdom.view.MainActivity
import com.alexmilovanov.randomwisdom.di.Scopes
import com.alexmilovanov.randomwisdom.view.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Module providing activities related dependencies
 */
@Module
internal abstract class ActivityModule {

    @Scopes.PerActivity
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeMainActivity(): MainActivity

}