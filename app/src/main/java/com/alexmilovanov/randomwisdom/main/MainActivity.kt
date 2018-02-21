package com.alexmilovanov.randomwisdom.main

import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import com.alexmilovanov.randomwisdom.uicommon.BaseActivity
import com.alexmilovanov.randomwisdom.util.ext.finishWithFadeOut
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import android.support.v4.view.ViewPager
import android.view.animation.DecelerateInterpolator
import com.alexmilovanov.randomwisdom.R
import com.alexmilovanov.randomwisdom.favorites.FavoriteQuotesFragment
import com.alexmilovanov.randomwisdom.mvibase.MviView
import com.alexmilovanov.randomwisdom.mvibase.MviViewModel
import com.alexmilovanov.randomwisdom.randomquote.RandomQuoteFragment
import com.alexmilovanov.randomwisdom.util.resources.IResourceProvider
import com.jakewharton.rxbinding2.view.RxView
import android.view.animation.AccelerateInterpolator


/**
 * Displays random quote screen.
 */
class MainActivity : BaseActivity() {

    @Inject
    lateinit var navigator: MainNavigator

    @Inject
    lateinit var resProvider: IResourceProvider

    private val pageChangeListener = PageChangeListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initTabs()
    }

    override fun onStart() {
        super.onStart()
        view_pager.addOnPageChangeListener(pageChangeListener)
   }

    override fun onStop() {
        super.onStop()
        view_pager.removeOnPageChangeListener(pageChangeListener)
    }

    override fun onBackPressed() {
        finishWithFadeOut()
    }

    /**
     * The Intent the [MviView] emit to convey to the [MviViewModel]
     * to request another random quote.
     */
    fun fabClickIntent() = RxView.clicks(fab)

    private fun initTabs() {
        val adapter = MainViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(RandomQuoteFragment.invoke(), resProvider.string(R.string.tab_random_title))
        adapter.addFragment(FavoriteQuotesFragment.invoke(), resProvider.string(R.string.tab_favorites_title))
        view_pager.adapter = adapter
        tab_layout.setupWithViewPager(view_pager)
    }

    /**
     * Display Fab by scrolling up to the screen
     */
    fun showFab(){
        fab.animate().translationY(0f).setInterpolator(DecelerateInterpolator(2f)).start();
    }

    /**
     * Hide Fab by scrolling off the screen
     */
    fun hideFab(){
        val lp = fab.layoutParams as CoordinatorLayout.LayoutParams
        val fabMargin = lp.bottomMargin
        fab.animate().translationY((fab.height + fabMargin).toFloat())
                .setInterpolator(AccelerateInterpolator(2f)).start()
    }


    inner class PageChangeListener : ViewPager.OnPageChangeListener {

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            // no-op
        }

        override fun onPageSelected(position: Int) {
            //Display Fab only if RandomQuote fragment is visible
            if(position == 0) {
                showFab()
            } else {
                hideFab()
            }
        }

        override fun onPageScrollStateChanged(state: Int) {
            // no-op
        }
    }
}