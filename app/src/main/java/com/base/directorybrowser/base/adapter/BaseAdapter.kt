package com.base.directorybrowser.base.adapter

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import com.base.directorybrowser.util.NO_VIEW_TYPE

open class BaseAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items = mutableListOf<RecyclerViewType>()
    lateinit var delegateAdapters: SparseArrayCompat<DelegateAdapter<RecyclerView.ViewHolder, RecyclerViewType>>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        delegateAdapters[viewType]?.let {
            return it.onCreateViewHolder(parent)
        } ?: run {
            throw IllegalArgumentException(NO_VIEW_TYPE)
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = items[position].getViewType()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        val delegateAdapter = delegateAdapters[item.getViewType()]
        if (delegateAdapter is DelegateAdapterWithPosition) {
            delegateAdapter.onBindViewHolderWithPosition(holder, item, position)
        } else {
            delegateAdapter?.onBindViewHolder(holder, item)
        }
    }

    open fun setElements(newListOfItems: List<RecyclerViewType>) {
        items.clear()
        items.addAll(newListOfItems)
        notifyDataSetChanged()
    }

    open fun addElements(newListOfItems: List<RecyclerViewType>) {
        items.addAll(newListOfItems)
        notifyDataSetChanged()
    }

    open fun setElementAtPosition(element: RecyclerViewType, position: Int) {
        if (position >= items.size || position < 0) return
        items[position] = element
        notifyItemChanged(position)
    }

    open fun getElements() = items

    open fun clear() {
        items.clear()
        notifyDataSetChanged()
    }
}