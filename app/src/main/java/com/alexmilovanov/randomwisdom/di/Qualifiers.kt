package com.alexmilovanov.randomwisdom.di

import javax.inject.Qualifier


/**
 * Qualifier annotation helping to distinguish between different objects of the same return type
 */
@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationContext