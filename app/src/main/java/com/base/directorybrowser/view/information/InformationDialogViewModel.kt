package com.base.directorybrowser.view.information

import androidx.lifecycle.MutableLiveData
import com.base.directorybrowser.base.BaseViewModel
import com.base.directorybrowser.base.adapter.RecyclerViewType
import com.base.directorybrowser.view.directory.DirectoryFileUiModel
import com.base.directorybrowser.view.directory.DirectoryUiModel
import com.base.directorybrowser.view.information.delegateAdapter.detail.DetailViewType
import javax.inject.Inject

class InformationDialogViewModel @Inject constructor() : BaseViewModel() {

    val typesMutable = MutableLiveData<List<RecyclerViewType>>()
    var recyclerTypes: MutableList<RecyclerViewType> = mutableListOf()

    fun init(item: DirectoryUiModel) {
        recyclerTypes.clear()
        when (item) {
            is DirectoryFileUiModel -> {
                val detailType = DetailViewType()
                detailType.itemInformation = item
                recyclerTypes.add(detailType)
                typesMutable.postValue(recyclerTypes)
            }
            else -> {
                //Do nothing
            }
        }

    }
}