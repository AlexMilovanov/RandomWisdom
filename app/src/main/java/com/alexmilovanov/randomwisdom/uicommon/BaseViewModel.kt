package com.alexmilovanov.randomwisdom.uicommon

import android.arch.lifecycle.ViewModel
import com.alexmilovanov.randomwisdom.mvibase.MviIntent
import com.alexmilovanov.randomwisdom.mvibase.MviViewModel
import com.alexmilovanov.randomwisdom.mvibase.MviViewState
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Base ViewModel class containing common functions and properties
 */
abstract class BaseViewModel<I : MviIntent, VS : MviViewState> : ViewModel(), MviViewModel<I, VS> {

    /**
     * Proxy subject used to keep the stream alive even after the UI gets recycled.
     * This is basically used to keep ongoing events and the last cached State alive
     * while the UI disconnects and reconnects on config changes.
     */
    protected val intentsSubject: PublishSubject<I> = PublishSubject.create()

    private val statesObservable by lazy { compose() }

    override fun processIntents(intents: Observable<I>) {
        intents.subscribe(intentsSubject)
    }

    override fun states(): Observable<VS> = statesObservable

    abstract fun compose() : Observable<VS>
}