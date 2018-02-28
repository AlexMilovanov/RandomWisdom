package com.alexmilovanov.randomwisdom.ui.splash

import android.os.Bundle
import com.alexmilovanov.randomwisdom.R
import com.alexmilovanov.randomwisdom.ui.common.BaseActivity
import javax.inject.Inject


/**
 * Holds a splash screen fragment.
 */
class SplashActivity : BaseActivity() {

    @Inject
    lateinit var navigator: SplashNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        savedInstanceState ?: navigator.navigateToSplashScreen()
    }

}