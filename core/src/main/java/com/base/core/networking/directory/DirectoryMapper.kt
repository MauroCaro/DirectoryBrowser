package com.base.core.networking.directory

import android.webkit.MimeTypeMap
import com.base.core.model.*
import com.dropbox.core.v2.files.FileMetadata
import com.dropbox.core.v2.files.ListFolderResult

interface IDirectoryMapper {
    @Throws(IllegalStateException::class)
    fun directoryNetworkModelToDomainModel(listFolder: ListFolderResult): List<DirectoryModel>
}

open class DirectoryMapper : IDirectoryMapper {
    override fun directoryNetworkModelToDomainModel(listFolder: ListFolderResult): List<DirectoryModel> {
        var listDirectoryModel = emptyList<DirectoryModel>()
        listFolder.entries.let {
            listDirectoryModel = it.map { item ->
                if (item is FileMetadata) {
                    DirectoryFileModel(
                        typFile = getTypeOfFile(item.name),
                        name = item.name,
                        id = item.id,
                        modificationDate = item.clientModified,
                        isDownloadable = item.isDownloadable,
                        idDownloadable = item.rev,
                        size = item.size,
                        pathDisplay = item.pathDisplay,
                        path = item.pathLower
                    )
                } else {
                    DirectoryFolderModel(
                        name = item.name,
                        pathDisplay = item.pathDisplay,
                        path = item.pathLower
                    )
                }
            }
        }
        return listDirectoryModel
    }

    private fun getTypeOfFile(name: String): TypeFile {
        val mimeType = MimeTypeMap.getSingleton()
        return when (name.substring(name.indexOf(".") + 1)) {
            "png", "jpg", "jpeg" -> {
                TypeFile.IMAGE
            }
            "pdf" -> {
                TypeFile.PDF
            }
            "docx" -> {
                TypeFile.WORD
            }
            "xls" -> {
                TypeFile.EXCEL
            }
            else -> {
                TypeFile.OTHER
            }
        }
    }
}
