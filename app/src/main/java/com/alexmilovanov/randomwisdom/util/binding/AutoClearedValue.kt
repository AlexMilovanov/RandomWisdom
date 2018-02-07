package com.alexmilovanov.randomwisdom.util.binding

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

/**
 * A value holder that automatically clears the reference if the Fragment's view is destroyed.
 */
class AutoClearedValue<T>(fragment: Fragment, var value: T?) {

    init {
        val fragManager = fragment.fragmentManager
        fragManager?.registerFragmentLifecycleCallbacks(
                object : FragmentManager.FragmentLifecycleCallbacks() {
                    override fun onFragmentDestroyed(fm: FragmentManager?, f: Fragment?) {
                        if (f === fragment) {
                            value = null
                            fragManager.unregisterFragmentLifecycleCallbacks(this)
                        }
                    }
                }, false);
    }

}
