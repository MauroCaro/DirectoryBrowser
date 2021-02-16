package com.base.core.networking.directory

import com.base.core.model.DirectoryFileModel
import com.base.core.model.DirectoryModel
import com.base.core.repository.DropboxDirectory
import com.dropbox.core.v2.files.ListFolderResult
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.inject.Inject


interface IDirectoryNetworkOperation {
    fun fetchUserFilesAndFolder(path: String): Observable<List<DirectoryModel>>
    fun downloadFile(item: DirectoryFileModel) : Observable<File>
}

@Suppress("UNCHECKED_CAST")
class DirectoryNetworkOperation @Inject constructor(
    private val directoryMapper: IDirectoryMapper,
    private val dropboxDirectory: DropboxDirectory
) : IDirectoryNetworkOperation {

    override fun fetchUserFilesAndFolder(path: String): Observable<List<DirectoryModel>> {
        val observable = dropboxDirectory.getListFolder(path)
            .map(parseLoginInformation)
            .subscribeOn(Schedulers.io())
        return observable as Observable<List<DirectoryModel>>
    }

    override fun downloadFile(item: DirectoryFileModel): Observable<File> {
        val observable = dropboxDirectory.getDownload(item)
            .subscribeOn(Schedulers.io())
        return observable as Observable<File>
    }

    private val parseLoginInformation = { data: ListFolderResult ->
        try {
            directoryMapper.directoryNetworkModelToDomainModel(data)
        } catch (e: IllegalStateException) {
            Observable.error<IllegalStateException>(e)
        }
    }

}