package com.alexmilovanov.randomwisdom.di

import javax.inject.Scope

/**
 * Custom scopes helping to encapsulate Dagger components
 */
class Scopes {

    //Scope that only lasts the duration of a fragment lifecycle
    @Scope
    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    annotation class PerFragment

    //Scope that only lasts the duration of an activity lifecycle
    @Scope
    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    annotation class PerActivity

}