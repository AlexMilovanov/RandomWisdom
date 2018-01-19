package com.alexmilovanov.randomwisdom.util.resources

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
constructor(@ApplicationContext private val ctx: Context) : IResourceProvider {

    override fun getString(@StringRes stringResId: Int): String = ctx.resources.getString(stringResId)

    override fun getInteger(@IntegerRes integerResId: Int): Int = ctx.resources.getInteger(integerResId)

    override fun getColor(@ColorRes colorResId: Int): Int = ContextCompat.getColor(ctx, colorResId);

}