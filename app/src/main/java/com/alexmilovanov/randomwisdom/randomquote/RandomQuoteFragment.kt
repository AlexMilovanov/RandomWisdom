package com.alexmilovanov.randomwisdom.randomquote

import android.animation.Animator
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import com.alexmilovanov.randomwisdom.R
import com.alexmilovanov.randomwisdom.data.persistence.quotes.Quote
import com.alexmilovanov.randomwisdom.databinding.FragmentRandomQuoteBinding
import com.alexmilovanov.randomwisdom.main.MainActivity
import com.alexmilovanov.randomwisdom.main.MainNavigator
import com.alexmilovanov.randomwisdom.uicommon.BaseFragment
import com.alexmilovanov.randomwisdom.mvibase.*
import com.alexmilovanov.randomwisdom.uicommon.AutoClearedValue
import com.alexmilovanov.randomwisdom.util.resources.ResourceProvider
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_random_quote.*
import javax.inject.Inject


/**
 * Main UI for the random curQuote screen.
 */
class RandomQuoteFragment : BaseFragment<RandomQuoteViewModel, RandomQuoteIntent, RandomQuoteViewState>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var resProvider: ResourceProvider

    @Inject
    lateinit var navigator: MainNavigator

    private val retryIntentPublisher = PublishSubject.create<RandomQuoteIntent.RetryIntent>()

    private lateinit var binding: AutoClearedValue<FragmentRandomQuoteBinding>

    private var animationInProgress = false

    private var dataAvailable = false

    private var curQuote: Quote? = null


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(ANIMATION_PROGRESS_KEY, animationInProgress)
        outState.putBoolean(DATA_AVAILABILITY_KEY, dataAvailable)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val dataBinding = FragmentRandomQuoteBinding.inflate(inflater, container, false)
        binding = AutoClearedValue(this, dataBinding)

        animationInProgress = savedInstanceState?.getBoolean(ANIMATION_PROGRESS_KEY) ?: false
        dataAvailable = savedInstanceState?.getBoolean(DATA_AVAILABILITY_KEY) ?: false

        return dataBinding.root
    }

    override fun initViewModel() =
            ViewModelProviders.of(this, viewModelFactory).get(RandomQuoteViewModel::class.java)

    override fun intents(): Observable<RandomQuoteIntent> =
            Observable.merge(listOf(
                    initialIntent(),
                    retryIntent(),
                    nextQuoteIntent(),
                    likeQuoteIntent(),
                    shareQuoteIntent()))

    override fun render(state: RandomQuoteViewState) {

        if (state.loading) {
            // Animate all current data off the screen while loading another piece
            if(curQuote != null)
               launchSlideOutAnimation()

            dataAvailable = false
        }

        state.error?.let {
            disposables.add(navigator.showErrorWithRetry(state.error.localizedMessage).subscribe {
                //Trigger RetryIntent upon Retry button click
                retryIntentPublisher.onNext(RandomQuoteIntent.RetryIntent)
            })
            // hide all data in case of an error
            gr_all_views.visibility = View.INVISIBLE
            curQuote = null
            dataAvailable = false
        }

        state.quote?.let {
            // Display data that was hidden before
            if(curQuote == null)
               gr_all_views.visibility = View.VISIBLE

            if(curQuote != state.quote){
                curQuote = state.quote
                // If out animation has finished, display new data and update a favorite icon
                if (!animationInProgress) {
                    displayNextQuote()
                    setFavIcon(state.isFavorite)
                }
            } else {
                // if quote data remains the same, then the quote was added or removed from favorites
                setFavIcon(state.isFavorite)
            }

            dataAvailable = true
        }
    }

    override fun subscribeToNavigationChanges() {
        val fragment = this@RandomQuoteFragment
        viewModel.run {
            shareCommand.observe(fragment,
                    Observer { text ->
                        text?.let {
                            navigator.shareText(text)
                        }
                    })
        }
    }

    /**
     * The initial Intent the [MviView] emit to convey to the [MviViewModel]
     * that it is ready to receive data.
     * This initial Intent is also used to pass any parameters the [MviViewModel] might need
     * to render the initial [MviViewState].
     */
    private fun initialIntent(): Observable<RandomQuoteIntent.InitialIntent> =
           Observable.just(RandomQuoteIntent.InitialIntent)

    /**
     * The Intent the [MviView] emit to convey to the [MviViewModel]
     * that it needs to retry loading next quote data
     */
    private fun retryIntent(): Observable<RandomQuoteIntent.RetryIntent> = retryIntentPublisher


    /**
     * The Intent the [MviView] emit to convey to the [MviViewModel]
     * to request another random curQuote.
     */
    private fun nextQuoteIntent(): Observable<RandomQuoteIntent.NextQuoteIntent> =
            (activity as MainActivity).fabClickIntent()
                    .map { RandomQuoteIntent.NextQuoteIntent }

    /**
     * Handle Favorite icon state with an appropriate animation
     */
    private fun setFavIcon(isFavorite: Boolean) {
        ib_like.apply {
            if (isFavorite) {
                when (tag) {
                    null, R.drawable.unlike_vector_anim -> {
                        setImageDrawable(resProvider.drawable(R.drawable.like_vector_anim))
                        (drawable as AnimatedVectorDrawable).start()
                        tag = R.drawable.like_vector_anim
                    }
                }
            } else {
                when (tag) {
                    null, R.drawable.like_vector_anim -> {
                        setImageDrawable(resProvider.drawable(R.drawable.unlike_vector_anim))
                        (drawable as AnimatedVectorDrawable).start()
                        tag = R.drawable.unlike_vector_anim
                    }
                }
            }
        }
    }

    /**
     * The Intent the [MviView] emit to convey to the [MviViewModel]
     * to add or remove curQuote from Favorites.
     */
    private fun likeQuoteIntent(): Observable<RandomQuoteIntent.LikeCurrentQuoteIntent> =
            RxView.clicks(ib_like).map {
                RandomQuoteIntent.LikeCurrentQuoteIntent(binding.value?.quote!!)
            }

    /**
     * The Intent the [MviView] emit to convey to the [MviViewModel]
     * to share selected curQuote.
     */
    private fun shareQuoteIntent(): Observable<RandomQuoteIntent.ShareCurrentQuoteIntent> =
            RxView.clicks(ib_share).map {
                RandomQuoteIntent.ShareCurrentQuoteIntent(binding.value?.quote!!)
            }

    /**
     * Animate logo while initial data is being loaded
     */
    private fun launchSlideOutAnimation() {
        YoYo.with(Techniques.SlideOutLeft).apply {
            duration(ANIM_DURATION)
            interpolate(AccelerateDecelerateInterpolator())
            withListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {
                    // no-op
                }

                // If data is available by the end of animation cycle, display a new piece and
                // set an appropriate "Favorite" button
                override fun onAnimationEnd(p0: Animator?) {
                    if (dataAvailable) {
                        displayNextQuote()
                        setFavIcon(false)
                    }
                    animationInProgress = false
                }

                override fun onAnimationCancel(p0: Animator?) {
                    // no-op
                }

                override fun onAnimationStart(p0: Animator?) {
                    animationInProgress = true
                }
            })
            playOn(tv_quote)
            playOn(tv_author)
            playOn(ib_like)
            playOn(ib_share)
        }
    }


    /**
     * Animate logo while initial data is being loaded
     */
    private fun launchSlideInAnimation() {
        YoYo.with(Techniques.SlideInRight).apply {
            duration(ANIM_DURATION)
            interpolate(AccelerateDecelerateInterpolator())
            playOn(tv_quote)
            playOn(tv_author)
            playOn(ib_like)
            playOn(ib_share)
        }
    }

    /**
     * Bind newly received quote object, start animation and enable action buttons
     */
    private fun displayNextQuote(){
        binding.value?.quote = curQuote
        launchSlideInAnimation()
    }

    companion object {

        const val ANIMATION_PROGRESS_KEY: String = "animationProgressTag"
        const val DATA_AVAILABILITY_KEY: String = "dataAvailabilityTag"

        const val ANIM_DURATION = 1000L

        operator fun invoke() = RandomQuoteFragment()
    }

}