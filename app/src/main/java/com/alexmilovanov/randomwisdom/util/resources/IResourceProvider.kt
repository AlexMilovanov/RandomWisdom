package com.alexmilovanov.randomwisdom.util.resources

import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.IntegerRes
import android.support.annotation.StringRes

/**
 * Defines methods providing access to app resources
 */
interface IResourceProvider {

    fun getString(@StringRes stringResId: Int): String

    fun getInteger(@IntegerRes integerResId: Int): Int

    fun getColor(@ColorRes colorResId: Int): Int

    fun getDrawable(@DrawableRes drawableResId: Int): Drawable
}