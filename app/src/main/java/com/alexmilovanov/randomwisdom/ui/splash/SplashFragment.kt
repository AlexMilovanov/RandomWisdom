package com.alexmilovanov.randomwisdom.ui.splash

import android.animation.Animator
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import com.alexmilovanov.randomwisdom.R
import com.alexmilovanov.randomwisdom.mvibase.*
import com.alexmilovanov.randomwisdom.ui.common.BaseFragment
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_splash.*
import javax.inject.Inject

/**
 * Main UI for a splash screen
 */
class SplashFragment : BaseFragment<SplashViewModel, AppLaunchIntent, SplashViewState>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navigator: SplashNavigator


    private var animationInProgress = false

    private var dataAvailable = false

    private val retryIntentPublisher = PublishSubject.create<AppLaunchIntent.RetryIntent>()


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun initViewModel() =
            ViewModelProviders.of(this, viewModelFactory).get(SplashViewModel::class.java)

    override fun intents(): Observable<AppLaunchIntent> = Observable.merge(initialIntent(), retryIntent())

    override fun render(state: SplashViewState) {
        if(state.loading) {
            if(!animationInProgress) {
                launchLogoAnimation()
                animationInProgress = true
            }
        }
        if (state.error != null) {
            disposables.add(navigator.showErrorWithRetry(state.error.localizedMessage).subscribe {
                //Trigger RetryIntent upon Retry button click
                retryIntentPublisher.onNext(AppLaunchIntent.RetryIntent)
            })
        }
        if(state.dataAvailable) {
            if(!animationInProgress)
               navigator.navigateToQuotes()
            else
               dataAvailable = true
        }
    }

    override fun subscribeToNavigationChanges() {
       // no-op
    }

    /**
     * The initial Intent the [MviView] emit to convey to the [MviViewModel]
     * that it is ready to receive data.
     * This initial Intent is also used to pass any parameters the [MviViewModel] might need
     * to render the initial [MviViewState] (e.g. the task id to load).
     */
    private fun initialIntent(): Observable<AppLaunchIntent.InitialQuotesIntent> {
        return Observable.just(AppLaunchIntent.InitialQuotesIntent)
    }

    /**
     * The Intent the [MviView] emit to convey to the [MviViewModel]
     * that it needs to retry loading initial data
     */
    private fun retryIntent(): Observable<AppLaunchIntent.RetryIntent> {
        return retryIntentPublisher
    }

    /**
     * Animate logo while initial data is being loaded
     */
    private fun launchLogoAnimation(){
        YoYo.with(Techniques.ZoomInUp).apply {
            duration(SPLASH_DURATION)
            interpolate(AccelerateDecelerateInterpolator())
            withListener(object : Animator.AnimatorListener{
                override fun onAnimationRepeat(p0: Animator?) {
                    // no-op
                }
                override fun onAnimationEnd(p0: Animator?) {
                    if(dataAvailable && animationInProgress){
                        navigator.navigateToQuotes()
                    }
                    animationInProgress = false
                }
                override fun onAnimationCancel(p0: Animator?) {
                    // no-op
                }
                override fun onAnimationStart(p0: Animator?) {
                    //launchTitleAnimation()
                }
            })
            playOn(iv_logo)
            playOn(iv_title)
        }
    }

    companion object {

        const val SPLASH_DURATION = 3000L

        operator fun invoke() = SplashFragment()
    }
}