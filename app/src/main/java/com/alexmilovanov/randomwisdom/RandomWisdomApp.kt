package com.alexmilovanov.randomwisdom

import com.alexmilovanov.randomwisdom.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

/**
 * An Application that injects its members and can be used to inject Android components attached to it
 */
class RandomWisdomApp: DaggerApplication() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Timber log-wrapper
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    // Initialize dagger application component
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
            DaggerAppComponent.builder().create(this)
}