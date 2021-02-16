package com.base.core.model

import java.util.*

abstract class DirectoryModel

class DirectoryFileModel(
        val type: Type = Type.FILE,
        var typFile: TypeFile,
        var name: String,
        var id: String,
        var modificationDate: Date?,
        var idDownloadable : String,
        var isDownloadable: Boolean,
        var size: Long,
        var pathDisplay: String,
        var path: String
) : DirectoryModel()

class DirectoryFolderModel(
    val type: Type = Type.FOLDER,
    var name: String,
    var pathDisplay: String,
    var path: String
) : DirectoryModel()

enum class Type {
    FILE,
    FOLDER
}

enum class TypeFile(val value: String) {
    IMAGE("Image"),
    PDF("Pdf"),
    WORD("Word"),
    EXCEL("Excel"),
    OTHER("No identified")
}

