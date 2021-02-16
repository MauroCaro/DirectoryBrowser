package com.base.directorybrowser.view.information.delegateAdapter.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.directorybrowser.R
import com.base.directorybrowser.base.adapter.DelegateAdapter
import kotlinx.android.synthetic.main.information_detail.view.*

class DetailDelegateAdapter : DelegateAdapter<DetailDelegateAdapter.ViewHolder, DetailViewType> {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(parent)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, viewType: DetailViewType) {
        viewHolder.bind(viewType)
    }

    class ViewHolder(viewGroup: ViewGroup) :
        RecyclerView.ViewHolder(
            LayoutInflater.from(viewGroup.context).inflate(R.layout.information_detail, viewGroup, false)
        ) {
        fun bind(detailItem: DetailViewType) {
            itemView.information_detail_name_text?.text = detailItem.itemInformation?.name
            itemView.information_detail_type_text?.text = detailItem.itemInformation?.typFile?.value
            itemView.information_detail_modification_text?.text = detailItem.itemInformation?.modificationDate.toString()
            itemView.information_detail_size_text?.text = detailItem.itemInformation?.size.toString()
            itemView.information_detail_location_text?.text = detailItem.itemInformation?.path
            itemView.information_detail_downloadable_text?.text = if (detailItem.itemInformation?.isDownloadable!!) "Yes" else "No"
        }
    }
}