package com.alexmilovanov.randomwisdom.ui.common

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

/**
 * A generic ViewHolder that works with a ViewDataBinding to bind the model class to the
 * corresponding item layout as defined in the layout xml.
 * @param <T> The type of the ViewDataBinding.
 */
open class DataBoundViewHolder<out V : ViewDataBinding> (val binding: V)
    : RecyclerView.ViewHolder(binding.root)
