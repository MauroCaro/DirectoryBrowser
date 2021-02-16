package com.base.directorybrowser.base.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface DelegateAdapter<VH : RecyclerView.ViewHolder, VT : RecyclerViewType> {
    fun onCreateViewHolder(parent: ViewGroup): VH
    fun onBindViewHolder(viewHolder: VH, viewType: VT)
}
