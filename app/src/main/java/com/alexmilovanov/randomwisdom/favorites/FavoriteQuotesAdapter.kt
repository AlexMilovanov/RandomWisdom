package com.alexmilovanov.randomwisdom.favorites

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alexmilovanov.randomwisdom.R
import com.alexmilovanov.randomwisdom.data.persistence.quotes.Quote
import com.alexmilovanov.randomwisdom.databinding.ItemQuoteBinding
import com.alexmilovanov.randomwisdom.uicommon.DataBoundListAdapter

/**
 * A RecyclerView adapter implementation populating favorite quotes list.
 */
class FavoriteQuotesAdapter (quotes: List<Quote>) : DataBoundListAdapter<Quote, ItemQuoteBinding> (
        quotes, Quote::class.java, Comparator { p0, p1 -> p0.timestamp.compareTo(p1.timestamp) }
) {

    override fun createBinding(parent: ViewGroup): ItemQuoteBinding {
        val binding: ItemQuoteBinding =
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_quote,
                        parent,
                        false)

        binding.root.setOnClickListener({
            val quote: Quote? = binding.quote
            quote?.let {
                itemClickSubject.onNext(quote)
            }
        })

        return binding
    }

    override fun bind(binding: ItemQuoteBinding, item: Quote) {
        binding.quote = item
    }

}


