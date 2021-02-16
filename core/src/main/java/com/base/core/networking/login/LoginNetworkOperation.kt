package com.base.core.networking.login

import com.base.core.model.UserInformationModel
import com.base.core.repository.DropboxAuthentication
import com.dropbox.core.v2.users.FullAccount
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


interface ILoginNetworkOperation {
    fun startOAuth2Authentication(key: String, scope: List<String>)
    fun fetchUserInformation(): Observable<UserInformationModel>
    fun hasTokenActive(): Boolean
}

@Suppress("UNCHECKED_CAST")
class LoginNetworkOperation @Inject constructor(
    private val loginInformationMapper: ILoginMapper,
    private val dropboxAuthentication: DropboxAuthentication
) : ILoginNetworkOperation {

    override fun startOAuth2Authentication(key: String, scope: List<String>) {
        dropboxAuthentication.startOAuth2Authentication(key, scope)
    }

    override fun fetchUserInformation(): Observable<UserInformationModel> {
        val observable = dropboxAuthentication.init()
            .map(parseLoginInformation)
            .subscribeOn(Schedulers.io())
        return observable as Observable<UserInformationModel>
    }

    override fun hasTokenActive(): Boolean {
        return dropboxAuthentication.hasToken()
    }

    private val parseLoginInformation = { data: Pair<FullAccount, String> ->
        try {
            loginInformationMapper.loginInformationNetworkModelToDomainModel(data.first, data.second)
        } catch (e: IllegalStateException) {
            Observable.error<IllegalStateException>(e)
        }
    }
}