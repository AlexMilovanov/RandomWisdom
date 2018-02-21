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

    fun string(@StringRes stringResId: Int): String

    fun integer(@IntegerRes integerResId: Int): Int

    fun color(@ColorRes colorResId: Int): Int

    fun drawable(@DrawableRes drawableResId: Int): Drawable
}