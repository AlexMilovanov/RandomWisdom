package com.alexmilovanov.randomwisdom.ui.splash

import com.alexmilovanov.randomwisdom.mvibase.MviViewState

/**
 * Represents possible splash screen view states
 */
data class SplashViewState(
        val dataAvailable: Boolean,
        val loading: Boolean,
        val error: Throwable?
) : MviViewState {

    companion object {
        fun idle() : SplashViewState {
            return SplashViewState(
                    false,
                    false,
                    null
            )
        }
    }

}