package com.alexmilovanov.randomwisdom.util.resources

import android.content.Context
import android.graphics.drawable.Drawable
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

    override fun drawable(drawableResId: Int): Drawable = ctx.resources.getDrawable(drawableResId, null)

    override fun string(@StringRes stringResId: Int): String = ctx.resources.getString(stringResId)

    override fun integer(@IntegerRes integerResId: Int): Int = ctx.resources.getInteger(integerResId)

    override fun color(@ColorRes colorResId: Int): Int = ContextCompat.getColor(ctx, colorResId);

}