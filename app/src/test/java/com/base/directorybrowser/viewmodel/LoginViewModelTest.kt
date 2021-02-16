package com.base.directorybrowser.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.base.core.model.UserInformationModel
import com.base.core.networking.login.ILoginNetworkOperation
import com.base.core.networking.login.LoginMapper
import com.base.directorybrowser.util.*
import com.base.directorybrowser.view.login.IUserInformationUiMapper
import com.base.directorybrowser.view.login.LoginViewModel
import com.base.directorybrowser.view.login.UserInformationUiModel
import io.reactivex.Observable
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.lang.IllegalStateException

@RunWith(PowerMockRunner::class)
@PrepareForTest(
    ILoginNetworkOperation::class,
    UserInformationUiModel::class,
    UserInformationModel::class,
    IUserInformationUiMapper::class
)
class LoginViewModelTest {

    private val userInformationObserver: Observer<Data<UserInformationUiModel>> = mock()
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userInformationUiModel: UserInformationUiModel
    private lateinit var userInformationModel: UserInformationModel

    @Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Rule
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var loginNetworkOperation: ILoginNetworkOperation

    @Mock
    private lateinit var userInformation: IUserInformationUiMapper

    @Mock
    private lateinit var loginMapper: LoginMapper

    @Captor
    private lateinit var userInformationCaptor: ArgumentCaptor<Data<UserInformationUiModel>>

    @Before
    fun setup() {
        userInformationUiModel = UserInformationUiModel("123", "test@test.con", "test", null)
        userInformationModel = UserInformationModel("123", "CO", null, "1", "test@test.con", "test", null)
        loginViewModel = LoginViewModel(loginNetworkOperation, userInformation).apply {
            getUserInformationLiveData().observeForever(userInformationObserver)
        }
    }

    @After
    fun tearDown() {
        loginViewModel.getUserInformationLiveData().removeObserver(userInformationObserver)
    }

    @Test
    fun `fetch user information successfully`() {
        Mockito.`when`(loginNetworkOperation.fetchUserInformation()).thenReturn(Observable.just(userInformationModel))
        Mockito.`when`(loginMapper.loginInformationNetworkModelToDomainModel(any(), any())).thenReturn(userInformationModel)
        Mockito.`when`(userInformation.domainModelToUiModel(any())).thenReturn(userInformationUiModel)

        loginViewModel.fetchInformation()

        userInformationCaptor.run {
            Mockito.verify(userInformationObserver, Mockito.times(2)).onChanged(capture())
            Assert.assertTrue(value.data?.token == "123")
            Assert.assertEquals(Status.SUCCESSFUL, value.responseType)
        }
    }

    @Test
    fun `fetch user information with error`() {
        Mockito.`when`(loginNetworkOperation.fetchUserInformation()).thenReturn(Observable.error(IllegalStateException("")))
        Mockito.`when`(loginMapper.loginInformationNetworkModelToDomainModel(any(), any())).thenReturn(userInformationModel)
        Mockito.`when`(userInformation.domainModelToUiModel(any())).thenReturn(userInformationUiModel)

        loginViewModel.fetchInformation()

        userInformationCaptor.run {
            Mockito.verify(userInformationObserver, Mockito.times(2)).onChanged(capture())
            Assert.assertEquals(Status.ERROR, value.responseType)
        }
    }
}