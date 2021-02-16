package com.base.directorybrowser.view.directory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.base.directorybrowser.BuildConfig
import com.base.directorybrowser.R
import com.base.directorybrowser.base.BaseActivity
import com.base.directorybrowser.dagger.injector.Injector
import com.base.directorybrowser.util.Data
import com.base.directorybrowser.util.Status
import com.base.directorybrowser.view.directory.adapter.DirectoryAdapter
import com.base.directorybrowser.view.information.InformationDialogFragment
import com.base.directorybrowser.view.information.InformationDialogFragment.Companion.BOTTOM_DIALOG_INFORMATION_FRAGMENT
import com.base.directorybrowser.view.information.InformationDialogViewModel
import kotlinx.android.synthetic.main.activity_directory.*
import java.io.File

class DirectoryActivity : BaseActivity() {

    private lateinit var directoryViewModel: DirectoryViewModel
    private lateinit var directoryAdapter: DirectoryAdapter
    private lateinit var informationDialogViewModel: InformationDialogViewModel
    private var directoryFileToDownload: DirectoryFileUiModel? = null
    private lateinit var path: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intentResult = intent.getStringExtra(EXTRA_PATH)
        path = intentResult?.let { it } ?: kotlin.run { "" }
        setContentView(R.layout.activity_directory)
        Injector.component().inject(this)
        directoryViewModel = ViewModelProvider(this, vmFactory).get(DirectoryViewModel::class.java)
        informationDialogViewModel = ViewModelProvider(this, vmFactory).get(InformationDialogViewModel::class.java)
        directoryViewModel.fetchUserFilesAndFolder(path)
        initModel()
        setupRecycler()
    }

    private fun initModel() {
        directoryViewModel.getUserFilesAndFoldersLiveData().observe(this, Observer(this::onUserFilesAndFoldersChanged))
        directoryViewModel.fileDownloadMutableLiveData.observe(this, Observer(this::onFileToViewOnExternalApp))
    }

    private fun setupRecycler() {
        directoryAdapter = DirectoryAdapter(::onFolderItemClicked, ::onFileItemClicked, ::onInformationItemClicked)
        directory_activity_recycler?.adapter = directoryAdapter
    }

    private fun onUserFilesAndFoldersChanged(folderAndFilesList: Data<List<DirectoryUiModel>>) {
        when (folderAndFilesList.responseType) {
            Status.LOADING -> {
            }
            Status.SUCCESSFUL -> {
                folderAndFilesList.data?.let {
                    directoryAdapter.setElements(it)
                }
            }
            Status.ERROR -> {
                Toast.makeText(this, R.string.directory_activity_error_getting_file_folders, Toast.LENGTH_SHORT).show()
            }
            Status.EMPTY -> {
                directory_activity_empty.visibility = View.VISIBLE
                directory_activity_recycler.visibility = View.GONE
            }
        }
    }

    private fun onFileToViewOnExternalApp(file: Data<File>) {
        when (file.responseType) {
            Status.LOADING -> {
            }
            Status.SUCCESSFUL -> {
                file.data?.let {
                    viewFileInExternalApp(it)
                }
            }
            Status.ERROR -> {
                Toast.makeText(this, R.string.directory_activity_error_download_file, Toast.LENGTH_SHORT).show()
            }
            Status.EMPTY -> {
                directory_activity_empty.visibility = View.VISIBLE
                directory_activity_recycler.visibility = View.GONE
            }
        }
    }

    private fun onFolderItemClicked(path: String) {
        val intent = Intent(this, DirectoryActivity::class.java)
        intent.putExtra(EXTRA_PATH, path)
        startActivity(intent)
    }

    private fun onFileItemClicked(item: DirectoryFileUiModel) {
        if (checkWritePermissions() && checkReadPermissions()) {
            directoryFileToDownload = item
            directoryViewModel.downloadFile(item)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                WRITE_STORAGE_CODE
            )
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_STORAGE_CODE
            )
        }
    }

    private fun onInformationItemClicked(item: DirectoryUiModel) {
        informationDialogViewModel.init(item)
        InformationDialogFragment().show(this.supportFragmentManager, BOTTOM_DIALOG_INFORMATION_FRAGMENT)
    }

    fun checkWritePermissions(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun checkReadPermissions(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            WRITE_STORAGE_CODE -> {
                directoryFileToDownload?.let {
                    directoryViewModel.downloadFile(it)
                }
            }
        }
    }

    private fun viewFileInExternalApp(result: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        val mime = MimeTypeMap.getSingleton()
        val ext = result.name.substring(result.name.indexOf(".") + 1)
        val type = mime.getMimeTypeFromExtension(ext)
        val data = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID +".provider", result)
        intent.setDataAndType(data,type);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent)
    }

    companion object {
        const val EXTRA_PATH = "Path"
        const val WRITE_STORAGE_CODE = 300
        const val READ_STORAGE_CODE = 400
    }
}