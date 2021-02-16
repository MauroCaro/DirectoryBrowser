package com.base.core.repository

import com.dropbox.core.oauth.DbxCredential
import com.dropbox.core.v2.DbxClientV2


class DropboxClientFactory {
    companion object {
        private var sDbxClient: DbxClientV2? = null

        fun init(accessToken: String?) {
            if (sDbxClient == null) {
                sDbxClient = DbxClientV2(DbxRequestConfigFactory.requestConfig, accessToken)
            }
        }

        fun init(credential: DbxCredential) {
            var credential = credential
            credential = DbxCredential(
                credential.accessToken,
                -1L,
                credential.refreshToken,
                credential.appKey
            )
            if (sDbxClient == null) {
                sDbxClient = DbxClientV2(DbxRequestConfigFactory.requestConfig, credential)
            }
        }

        fun getClient(): DbxClientV2? {
            checkNotNull(sDbxClient) { "Client not initialized." }
            return sDbxClient
        }
    }
}