package com.alexmilovanov.randomwisdom.di.ui

import com.alexmilovanov.randomwisdom.view.ui.MainActivity
import com.alexmilovanov.randomwisdom.di.Scopes
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Module providing activities related dependencies
 */
@Module
internal abstract class ActivityModule {

    @Scopes.PerActivity
    @ContributesAndroidInjector()
    abstract fun contributeMainActivity(): MainActivity
}