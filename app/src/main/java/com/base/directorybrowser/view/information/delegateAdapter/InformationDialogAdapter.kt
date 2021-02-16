package com.base.directorybrowser.view.information.delegateAdapter

import androidx.collection.SparseArrayCompat
import com.base.directorybrowser.base.adapter.BaseAdapter
import com.base.directorybrowser.base.adapter.appendDelegate
import com.base.directorybrowser.view.information.delegateAdapter.detail.DetailDelegateAdapter
import com.base.directorybrowser.view.information.delegateAdapter.detail.DetailViewType

class InformationDialogAdapter : BaseAdapter() {
    init {
        delegateAdapters = SparseArrayCompat()
        delegateAdapters.appendDelegate(DetailViewType.DETAIL_ITEM.hashCode(), DetailDelegateAdapter())
    }
}