package com.alexmilovanov.randomwisdom.util.schedulers

import io.reactivex.Scheduler

/**
 * Defines methods for providing different types of [Scheduler]s.
 */
interface ISchedulerProvider {

    fun computation(): Scheduler

    fun io(): Scheduler

    fun ui(): Scheduler

}