package com.base.directorybrowser.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.base.core.model.*
import com.base.core.networking.directory.DirectoryMapper
import com.base.core.networking.directory.IDirectoryNetworkOperation
import com.base.directorybrowser.util.*
import com.base.directorybrowser.view.directory.*
import io.reactivex.Observable
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.powermock.modules.junit4.PowerMockRunner
import java.lang.IllegalStateException
import java.time.Instant
import java.util.*

@RunWith(PowerMockRunner::class)
class DirectoryViewModelTest {

    private val userFolderAnFilesObserver: Observer<Data<List<DirectoryUiModel>>> = mock()
    private lateinit var directoryViewModel: DirectoryViewModel
    private lateinit var directoryFileUiModel: DirectoryFileUiModel
    private lateinit var directoryFolderUiModel: DirectoryFolderUiModel
    private lateinit var directoryFileModel: DirectoryFileModel
    private lateinit var directoryFolderModel: DirectoryFolderModel

    @Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Rule
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var directoryNetworkOperation: IDirectoryNetworkOperation

    @Mock
    private lateinit var directoryUiMapper: IDirectoryUiMapper

    @Mock
    private lateinit var directoryMapper: DirectoryMapper

    @Captor
    private lateinit var userFileAndFilesCaptor: ArgumentCaptor<Data<List<DirectoryUiModel>>>

    @Before
    fun setup() {
        directoryFileUiModel = DirectoryFileUiModel(
            Type.FILE, TypeFile.PDF, "fileUiModel", "1", Date.from(Instant.now()), "123", true, 12L, "home", "home", 0
        )
        directoryFileModel = DirectoryFileModel(
            Type.FILE, TypeFile.PDF, "fileModel", "1", Date.from(Instant.now()), "123", true, 12L, "home", "home"
        )
        directoryFolderUiModel = DirectoryFolderUiModel(
            Type.FOLDER, "folderUiModel", "home", "home", 0
        )

        directoryFolderModel = DirectoryFolderModel(
            Type.FOLDER, "folderModel", "home", "home"
        )
        directoryViewModel = DirectoryViewModel(directoryNetworkOperation, directoryUiMapper).apply {
            getUserFilesAndFoldersLiveData().observeForever(userFolderAnFilesObserver)
        }
    }

    @After
    fun tearDown() {
        directoryViewModel.getUserFilesAndFoldersLiveData().removeObserver(userFolderAnFilesObserver)
    }

    @Test
    fun `fetch user files and folder successfully`() {
        val listDirectoryModel = listOf(directoryFolderModel, directoryFileModel)
        val listDirectoryUiModel = listOf(directoryFolderUiModel, directoryFileUiModel)
        Mockito.`when`(directoryNetworkOperation.fetchUserFilesAndFolder(any())).thenReturn(Observable.just(listDirectoryModel))
        Mockito.`when`(directoryMapper.directoryNetworkModelToDomainModel(any())).thenReturn(listDirectoryModel)
        Mockito.`when`(directoryUiMapper.domainModelToUiModel(any())).thenReturn(listDirectoryUiModel)

        directoryViewModel.fetchUserFilesAndFolder("")

        userFileAndFilesCaptor.run {
            Mockito.verify(userFolderAnFilesObserver, Mockito.times(2)).onChanged(capture())
            Assert.assertTrue(value.data?.size == 2)
            Assert.assertTrue(value.data?.get(0)?.type == Type.FOLDER)
            Assert.assertTrue(value.data?.get(1)?.type == Type.FILE)
            Assert.assertEquals(Status.SUCCESSFUL, value.responseType)
        }
    }

    @Test
    fun `fetch user files and folder error`() {
        val listDirectoryModel = listOf(directoryFolderModel, directoryFileModel)
        val listDirectoryUiModel = listOf(directoryFolderUiModel, directoryFileUiModel)
        Mockito.`when`(directoryNetworkOperation.fetchUserFilesAndFolder(any())).thenReturn(Observable.error(IllegalStateException("")))
        Mockito.`when`(directoryMapper.directoryNetworkModelToDomainModel(any())).thenReturn(listDirectoryModel)
        Mockito.`when`(directoryUiMapper.domainModelToUiModel(any())).thenReturn(listDirectoryUiModel)

        directoryViewModel.fetchUserFilesAndFolder("")

        userFileAndFilesCaptor.run {
            Mockito.verify(userFolderAnFilesObserver, Mockito.times(2)).onChanged(capture())
            Assert.assertEquals(Status.ERROR, value.responseType)
        }
    }

    @Test
    fun `fetch user files and folder with emtpy information`() {
        val listDirectoryModel = emptyList<DirectoryModel>()
        val listDirectoryUiModel = emptyList<DirectoryUiModel>()
        Mockito.`when`(directoryNetworkOperation.fetchUserFilesAndFolder(any())).thenReturn(Observable.just(listDirectoryModel))
        Mockito.`when`(directoryMapper.directoryNetworkModelToDomainModel(any())).thenReturn(listDirectoryModel)
        Mockito.`when`(directoryUiMapper.domainModelToUiModel(any())).thenReturn(listDirectoryUiModel)

        directoryViewModel.fetchUserFilesAndFolder("")

        userFileAndFilesCaptor.run {
            Mockito.verify(userFolderAnFilesObserver, Mockito.times(2)).onChanged(capture())
            Assert.assertEquals(Status.EMPTY, value.responseType)
        }
    }
}