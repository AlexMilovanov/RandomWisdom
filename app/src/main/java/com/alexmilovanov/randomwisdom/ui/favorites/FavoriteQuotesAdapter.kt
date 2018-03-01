package com.alexmilovanov.randomwisdom.ui.favorites

import android.databinding.DataBindingUtil
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alexmilovanov.randomwisdom.R
import com.alexmilovanov.randomwisdom.data.persistence.quotes.Quote
import com.alexmilovanov.randomwisdom.databinding.ItemQuoteBinding
import com.alexmilovanov.randomwisdom.ui.common.DataBoundListAdapter
import com.alexmilovanov.randomwisdom.ui.common.DataBoundViewHolder
import kotlinx.android.synthetic.main.item_quote.view.*

/**
 * A RecyclerView adapter implementation populating favorite quotes list.
 */
class FavoriteQuotesAdapter (quotes: List<Quote>) : DataBoundListAdapter<Quote, ItemQuoteBinding> (
        quotes, Quote::class.java, Comparator { q0, q1 -> q1.timestamp.compareTo(q0.timestamp) }
) {

    override fun createBinding(parent: ViewGroup): ItemQuoteBinding {
        val binding: ItemQuoteBinding =
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_quote,
                        parent,
                        false)

        binding.root.cv_quote.setOnClickListener({
            val quote: Quote? = binding.quote
            quote?.let {
                itemClickSubject.onNext(quote)
            }
        })

        return binding
    }

    /**
     * Override to deliver custom implementation of DataBoundViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder<ItemQuoteBinding> =
        ViewHolderRecyclerViewAnimation(createBinding(parent))


    override fun bind(binding: ItemQuoteBinding, item: Quote) {
        binding.quote = item
    }

    class ViewHolderRecyclerViewAnimation (binding: ItemQuoteBinding)
        : DataBoundViewHolder <ItemQuoteBinding> (binding) {

        // Layout displayed when the user does a swipe
        val viewBackground = binding.root.findViewById<View>(R.id.v_swipe_dismiss)
        // Layout shown to the user by default.
        val cardViewForeground = binding.root.findViewById<CardView>(R.id.cv_quote)
    }

}


