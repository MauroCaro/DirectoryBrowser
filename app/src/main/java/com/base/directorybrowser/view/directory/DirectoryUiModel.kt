package com.base.directorybrowser.view.directory

import com.base.core.model.Type
import com.base.core.model.TypeFile
import java.util.*

abstract class DirectoryUiModel(open val type: Type)

class DirectoryFileUiModel(
    override val type: Type = Type.FILE,
    var typFile: TypeFile,
    var name: String,
    var id: String,
    var modificationDate: Date?,
    var idDownloadable : String,
    var isDownloadable: Boolean,
    var size: Long,
    var pathDisplay: String,
    var path: String,
    var drawableImage : Int

) : DirectoryUiModel(type)

class DirectoryFolderUiModel(
    override val type: Type = Type.FOLDER,
    var name: String,
    var pathDisplay: String,
    var path: String,
    var drawableImage : Int
) : DirectoryUiModel(type)
