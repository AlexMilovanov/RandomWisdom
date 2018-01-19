package com.alexmilovanov.randomwisdom.util.resources

import android.support.annotation.ColorRes
import android.support.annotation.IntegerRes
import android.support.annotation.StringRes

/**
 * Defines methods providing access to app resources
 */
interface IResourceProvider {

    fun getString(@StringRes stringResId: Int): String

    fun getInteger(@IntegerRes integerResId: Int): Int

    fun getColor(@ColorRes colorResId: Int): Int
}