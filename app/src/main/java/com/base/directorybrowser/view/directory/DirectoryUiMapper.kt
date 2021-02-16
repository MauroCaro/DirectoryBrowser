package com.base.directorybrowser.view.directory

import com.base.core.model.DirectoryFileModel
import com.base.core.model.DirectoryFolderModel
import com.base.core.model.DirectoryModel
import com.base.core.model.TypeFile
import com.base.directorybrowser.R


interface IDirectoryUiMapper {
    fun domainModelToUiModel(listDirectoryModel: List<DirectoryModel>): List<DirectoryUiModel>
    fun directoryFileUiModelToDomainModel(item: DirectoryFileUiModel): DirectoryFileModel
}

class DirectoryUiMapper : IDirectoryUiMapper {
    override fun domainModelToUiModel(listDirectoryModel: List<DirectoryModel>): List<DirectoryUiModel> {
        val listDirectoryUiModel: List<DirectoryUiModel>
        listDirectoryUiModel = listDirectoryModel.map { item ->
            if (item is DirectoryFileModel) {
                DirectoryFileUiModel(
                    typFile = item.typFile,
                    name = item.name,
                    id = item.id,
                    modificationDate = item.modificationDate,
                    idDownloadable = item.idDownloadable,
                    isDownloadable = item.isDownloadable,
                    size = item.size,
                    pathDisplay = item.pathDisplay,
                    path = item.path,
                    drawableImage = getDrawableImageBaseOnFileType(item.typFile)
                )
            } else {
                DirectoryFolderUiModel(
                    name = (item as DirectoryFolderModel).name,
                    pathDisplay = item.pathDisplay,
                    path = item.path,
                    drawableImage = R.drawable.folder
                )
            }
        }
        return listDirectoryUiModel
    }

    override fun directoryFileUiModelToDomainModel(item: DirectoryFileUiModel): DirectoryFileModel {
        return DirectoryFileModel(
            typFile = item.typFile,
            name = item.name,
            id = item.id,
            modificationDate = item.modificationDate,
            isDownloadable = item.isDownloadable,
            idDownloadable = item.idDownloadable,
            size = item.size,
            pathDisplay = item.pathDisplay,
            path = item.path
        )
    }

    private fun getDrawableImageBaseOnFileType(typFile: TypeFile): Int {
        return when (typFile) {
            TypeFile.IMAGE -> R.drawable.image
            TypeFile.PDF -> R.drawable.pdf
            TypeFile.EXCEL -> R.drawable.excel
            TypeFile.WORD -> R.drawable.word
            TypeFile.OTHER -> R.drawable.other_file
        }
    }
}