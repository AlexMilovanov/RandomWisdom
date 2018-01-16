package com.alexmilovanov.randomwisdom.view.util

import android.content.Context
import android.support.annotation.ColorRes
import android.support.annotation.IntegerRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import com.alexmilovanov.randomwisdom.di.ApplicationContext
import javax.inject.Inject


// Util class providing access to app resources
class ResourceProvider
@Inject
constructor(@ApplicationContext private val ctx: Context) {

    fun getString(@StringRes stringResId: Int): String = ctx.resources.getString(stringResId)

    fun getInteger(@IntegerRes integerResId: Int): Int = ctx.resources.getInteger(integerResId)

    fun getColor(@ColorRes colorResId: Int): Int = ContextCompat.getColor(ctx, colorResId);

}