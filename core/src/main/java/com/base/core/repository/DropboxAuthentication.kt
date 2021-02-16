package com.base.core.repository

import android.content.Context
import android.content.SharedPreferences
import com.dropbox.core.android.Auth
import com.dropbox.core.json.JsonReadException
import com.dropbox.core.oauth.DbxCredential
import com.dropbox.core.v2.users.FullAccount
import io.reactivex.Observable


class DropboxAuthentication(
    private val applicationContext: Context,
    private val prefs: SharedPreferences
) {
    // will use our Short Lived Token.
    private val USE_SLT: Boolean = true

    private lateinit var credentialToken: String


    fun init(): Observable<Pair<FullAccount, String>> {
        if (USE_SLT) {
            val serailizedCredental = prefs.getString("credential", null)
            if (serailizedCredental == null) {
                val credential = Auth.getDbxCredential()
                if (credential != null) {
                    prefs.edit().putString("credential", credential.toString()).apply()
                    credentialToken = credential.accessToken
                    initAndLoadData(credential)
                }
            } else {
                try {
                    val credential = DbxCredential.Reader.readFully(serailizedCredental)
                    initAndLoadData(credential)
                    credentialToken = credential.accessToken
                } catch (e: JsonReadException) {
                    throw IllegalStateException("Credential data corrupted: " + e.message)
                }
            }
        }
        val uid = Auth.getUid()
        val storedUid = prefs.getString("user-id", null)
        if (uid != null && uid != storedUid) {
            prefs.edit().putString("user-id", uid).apply()
        }
        return Observable.create<Pair<FullAccount, String>> { emitter ->
            DropboxClientFactory.getClient()?.users()?.currentAccount?.let {
                emitter.onNext(Pair(it, credentialToken))
                emitter.onComplete()
            } ?: kotlin.run {
                emitter.onError(Throwable("Error getting acount information"))
            }
        }
    }

    private fun initAndLoadData(dbxCredential: DbxCredential) {
        DropboxClientFactory.init(dbxCredential)
    }

    fun hasToken(): Boolean {
        return prefs.getString("credential", null) != null
    }

    fun startOAuth2Authentication(
        app_key: String?,
        scope: List<String?>?
    ) {
        if (USE_SLT) {
            Auth.startOAuth2PKCE(
                applicationContext,
                app_key,
                DbxRequestConfigFactory.requestConfig,
                scope
            )
        } else {
            Auth.startOAuth2Authentication(applicationContext, app_key)
        }
    }
}