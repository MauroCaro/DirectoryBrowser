package com.base.directorybrowser.view.directory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.base.core.networking.directory.IDirectoryNetworkOperation
import com.base.directorybrowser.base.BaseViewModel
import com.base.directorybrowser.util.Data
import com.base.directorybrowser.util.Status
import com.base.directorybrowser.view.login.UserInformationUiModel
import com.dropbox.core.v2.files.ListFolderErrorException
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.File
import javax.inject.Inject

class DirectoryViewModel @Inject constructor(
    private val directoryNetworkOperation: IDirectoryNetworkOperation,
    private val directoryUiMapper: IDirectoryUiMapper
) : BaseViewModel() {

    private val userFilesAndFoldersMutableLiveData = MutableLiveData<Data<List<DirectoryUiModel>>>()
    val fileDownloadMutableLiveData = MutableLiveData<Data<File>>()

    fun getUserFilesAndFoldersLiveData(): LiveData<Data<List<DirectoryUiModel>>> = userFilesAndFoldersMutableLiveData

    fun fetchUserFilesAndFolder(path: String) {
        compositeDisposable.add(
            directoryNetworkOperation.fetchUserFilesAndFolder(path)
                .doOnSubscribe {
                    userFilesAndFoldersMutableLiveData.postValue(Data(responseType = Status.LOADING))
                }
                .map {
                    directoryUiMapper.domainModelToUiModel(it)
                }

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.isEmpty()) {
                        userFilesAndFoldersMutableLiveData.postValue(Data(responseType = Status.EMPTY))
                    } else {
                        userFilesAndFoldersMutableLiveData.postValue(Data(responseType = Status.SUCCESSFUL, data = response))
                    }
                }, {
                    if (it is ListFolderErrorException) {
                        userFilesAndFoldersMutableLiveData.postValue(Data(responseType = Status.EMPTY))
                    }
                    userFilesAndFoldersMutableLiveData.postValue(Data(responseType = Status.ERROR))
                })
        )
    }

    fun downloadFile(item: DirectoryFileUiModel) {
        compositeDisposable.add(
            directoryNetworkOperation.downloadFile(directoryUiMapper.directoryFileUiModelToDomainModel(item))
                .doOnSubscribe {
                    userFilesAndFoldersMutableLiveData.postValue(Data(responseType = Status.LOADING))
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    fileDownloadMutableLiveData.postValue(Data(responseType = Status.SUCCESSFUL, data = response))
                }, {
                    fileDownloadMutableLiveData.postValue(Data(responseType = Status.ERROR))
                })
        )
    }
}