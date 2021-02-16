package com.base.core.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import com.base.core.model.DirectoryFileModel
import com.base.core.util.Messages
import com.dropbox.core.DbxException
import com.dropbox.core.v2.files.ListFolderErrorException
import com.dropbox.core.v2.files.ListFolderResult
import io.reactivex.Observable
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DropboxDirectory(private val context: Context) {

    fun getListFolder(path: String): Observable<ListFolderResult> {
        return Observable.create<ListFolderResult> { emitter ->
            try {
                DropboxClientFactory.getClient()?.files()?.listFolder(path)?.let {
                    emitter.onNext(it)
                    emitter.onComplete()
                } ?: kotlin.run {
                    emitter.onError(Throwable(Messages.DIRECTORY_DATA_ERROR_REPOSITORY.message))
                }
            } catch (exception: ListFolderErrorException) {
                emitter.onError(ListFolderErrorException(exception.message, exception.requestId, exception.userMessage, exception.errorValue))
            }
        }
    }

    fun getDownload(metadata: DirectoryFileModel): Observable<File?> {
        return Observable.create<File> { emitter ->
            try {
                val path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!
                val file = File(path, metadata.name)

                // Make sure the Downloads directory exists.
                if (!path.exists()) {
                    if (!path.mkdirs()) {
                        emitter.onError(RuntimeException("Unable to create directory: $path"))
                    }
                } else if (!path.isDirectory) {
                    emitter.onError(IllegalStateException("Download path is not a directory: $path"))
                }
                FileOutputStream(file).use { outputStream ->
                    DropboxClientFactory.getClient()?.files()?.download(metadata.path, metadata.idDownloadable)?.download(outputStream)
                }

                // Tell android about the file
                val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                intent.data = Uri.fromFile(file)
                context.sendBroadcast(intent)
                emitter.onNext(file)
                emitter.onComplete()
            } catch (dbxException: DbxException) {
                emitter.onError(dbxException)
            } catch (ioException: IOException) {
                emitter.onError(ioException)
            }
        }
    }
}