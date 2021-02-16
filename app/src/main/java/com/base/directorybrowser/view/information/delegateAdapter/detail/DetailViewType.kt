package com.base.directorybrowser.view.information.delegateAdapter.detail

import com.base.directorybrowser.base.adapter.RecyclerViewType
import com.base.directorybrowser.view.directory.DirectoryFileUiModel

class DetailViewType : RecyclerViewType {

    override fun getViewType() = DETAIL_ITEM.hashCode()

    var itemInformation: DirectoryFileUiModel? = null
    companion object {
        const val DETAIL_ITEM = "detail_delegate_item"
    }
}