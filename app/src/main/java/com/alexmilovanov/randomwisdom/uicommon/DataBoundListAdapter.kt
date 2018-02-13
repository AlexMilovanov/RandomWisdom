package com.alexmilovanov.randomwisdom.uicommon

import android.databinding.ViewDataBinding
import android.support.v7.util.SortedList
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


/**
 * A generic RecyclerView adapter that uses Data Binding
 * @param <T> Type of the items in the list
 * @param <V> Type of the ViewDataBinding
 */
abstract class DataBoundListAdapter<T, V : ViewDataBinding>(
        listItems: List<T>, contentClass: Class<T>, comparator: Comparator<T>
) : RecyclerView.Adapter<DataBoundViewHolder<V>>() {

    protected val itemClickSubject = PublishSubject.create<T>()

    val itemClickObservable: Observable<T>
        get() = itemClickSubject

    /**
     * A Sorted list implementation that can keep items in order using the Comparator and
     * uses binary search to retrieve items. Notifies for changes in the
     * list such that it can be bound to a RecyclerViewAdapter.
     */
    private val sortedItems: SortedList<T> = SortedList<T>(
            contentClass,
            //SortedList interacts with the Adapter through a Callback class which needs to be implemented
            object : SortedList.Callback<T>() {

                override fun onInserted(position: Int, count: Int) {
                    notifyItemRangeInserted(position, count)
                }

                override fun onChanged(position: Int, count: Int) {
                    notifyItemRangeChanged(position, count)
                }

                override fun onRemoved(position: Int, count: Int) {
                    notifyItemRangeRemoved(position, count)
                }

                override fun onMoved(fromPosition: Int, toPosition: Int) {
                    notifyItemMoved(fromPosition, toPosition)
                }

                // Check items against referential equality
                override fun areItemsTheSame(item1: T, item2: T): Boolean {
                    return item1 === item2
                }

                // Determine if the content of a model has changed
                override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
                    return oldItem == newItem
                }

                // Compares two models in order to make the list items appear in the appropriate order
                override fun compare(o1: T, o2: T): Int {
                    return comparator.compare(o1, o2)
                }
            })

    init {
        add(listItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder<V> {
        val binding = createBinding(parent)
        return DataBoundViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder<V>, position: Int) {
        bind(holder.binding, sortedItems[position])
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = sortedItems.size()

    fun getItem(index: Int): T = sortedItems[index]

    // Helper methods to make changes to the current list
    fun add(model: T) = sortedItems.add(model)

    fun remove(model: T) = sortedItems.remove(model)

    fun add(models: List<T>) = sortedItems.addAll(models)

    // Since the SortedList has only one remove method which can remove a single object
    // we need to loop over the list and remove the models one by one.
    fun remove(models: List<T>) {
        // Batches all the changes together and improves performance
        sortedItems.beginBatchedUpdates()
        for (model in models) {
            sortedItems.remove(model)
        }
        // The RecyclerView is notified about all the changes at once.
        sortedItems.endBatchedUpdates()
    }

    // Replace all items in the RecyclerView at once.
    // Remove everything which is not in the List and add all items which are missing from the SortedList
    fun replaceAll(items: List<T>) {
        sortedItems.apply {
            beginBatchedUpdates()
            clear()
            addAll(items)
            endBatchedUpdates()
        }
    }

    protected abstract fun createBinding(parent: ViewGroup): V

    protected abstract fun bind(binding: V, item: T)

}