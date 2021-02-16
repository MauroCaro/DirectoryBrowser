package com.base.directorybrowser.base.adapter

import androidx.recyclerview.widget.RecyclerView

abstract class DelegateAdapterWithPosition<VH : RecyclerView.ViewHolder, VT : RecyclerViewType> :
    DelegateAdapter<VH, VT> {

    abstract fun onBindViewHolderWithPosition(viewHolder: VH, viewType: VT, position: Int)
    override fun onBindViewHolder(viewHolder: VH, viewType: VT) {
        // Do Nothing
    }
}