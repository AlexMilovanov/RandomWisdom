package com.alexmilovanov.randomwisdom.binding

import android.databinding.DataBindingComponent
import android.support.v4.app.Fragment
import com.alexmilovanov.randomwisdom.ui.randomquote.RandomQuoteFragment


/**
 * A Data Binding Component implementation for fragments.
 */
class FragmentDataBindingComponent <out T : Fragment?>(val fragment: T) : DataBindingComponent {

    override fun getRandomQuoteFragment(): RandomQuoteFragment? = fragment as RandomQuoteFragment

}