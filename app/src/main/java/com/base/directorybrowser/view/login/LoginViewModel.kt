package com.base.directorybrowser.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.base.core.networking.login.ILoginNetworkOperation
import com.base.directorybrowser.base.BaseViewModel
import com.base.directorybrowser.util.Data
import com.base.directorybrowser.util.Status
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject
import javax.inject.Named

class LoginViewModel @Inject constructor(
    private val loginNetworkOperation: ILoginNetworkOperation,
    private val userInformationUiMapper: IUserInformationUiMapper
) : BaseViewModel() {

    @field:[Inject Named("key")]
    lateinit var key: String

    private val userInformationMutableLiveData = MutableLiveData<Data<UserInformationUiModel>>()
    fun getUserInformationLiveData(): LiveData<Data<UserInformationUiModel>> = userInformationMutableLiveData


    fun userHasTokenActive(): Boolean {
        return loginNetworkOperation.hasTokenActive()
    }

    fun fetchInformation() {
        compositeDisposable.add(
            loginNetworkOperation.fetchUserInformation()
                .doOnSubscribe {
                    userInformationMutableLiveData.postValue(Data(responseType = Status.LOADING))
                }
                .map {
                    userInformationUiMapper.domainModelToUiModel(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    userInformationMutableLiveData.postValue(Data(responseType = Status.SUCCESSFUL, data = response))
                }, {
                    userInformationMutableLiveData.postValue(Data(responseType = Status.ERROR))
                })
        )
    }

    fun login() {
        loginNetworkOperation.startOAuth2Authentication(
            key,
            listOf("account_info.read", "files.content.write", "files.content.read")
        )
    }
}