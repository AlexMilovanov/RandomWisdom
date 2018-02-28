package com.alexmilovanov.randomwisdom.ui.favorites

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper


/**
 * Handles the swipe left behaviour of the items in the [RecyclerView].
 * to use this in your code just extend a class with ItemTouchHelper.SimpleCallback.
 */
class SwipeDismissItemTouchHelper(dragDirs: Int, swipeDirs: Int, private val callback: ItemSwipedCallback)
    : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    /**
     * Not needed in this implementation
     */
    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder?) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        callback.onSwipe(viewHolder.adapterPosition)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        viewHolder?.let {
            val foregroundView = (viewHolder as FavoriteQuotesAdapter.ViewHolderRecyclerViewAnimation).cardViewForeground
            ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(foregroundView)
        }
    }

    override fun onChildDrawOver(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val foregroundView = (viewHolder as FavoriteQuotesAdapter.ViewHolderRecyclerViewAnimation).cardViewForeground
        ItemTouchHelper.Callback.getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
    }

    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder) {
        val foregroundView = (viewHolder as FavoriteQuotesAdapter.ViewHolderRecyclerViewAnimation).cardViewForeground
        ItemTouchHelper.Callback.getDefaultUIUtil().clearView(foregroundView)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val foregroundView = (viewHolder as FavoriteQuotesAdapter.ViewHolderRecyclerViewAnimation).cardViewForeground
        ItemTouchHelper.Callback.getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
    }

    interface ItemSwipedCallback {
        fun onSwipe(position: Int)
    }
}