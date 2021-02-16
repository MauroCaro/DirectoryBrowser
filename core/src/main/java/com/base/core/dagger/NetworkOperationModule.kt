package com.base.core.dagger

import android.content.Context
import android.content.SharedPreferences
import com.base.core.networking.directory.DirectoryMapper
import com.base.core.networking.directory.DirectoryNetworkOperation
import com.base.core.networking.directory.IDirectoryNetworkOperation
import com.base.core.networking.login.ILoginNetworkOperation
import com.base.core.networking.login.LoginMapper
import com.base.core.networking.login.LoginNetworkOperation
import com.base.core.repository.DropboxAuthentication
import com.base.core.repository.DropboxDirectory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkOperationModule(val app: Context, val pref: SharedPreferences) {

    @Provides
    @Singleton
    internal fun provideLoginNetworkOperation(): ILoginNetworkOperation {
        return LoginNetworkOperation(LoginMapper(), DropboxAuthentication(app, pref))
    }


    @Provides
    @Singleton
    internal fun provideDirectoryNetworkOperation(): IDirectoryNetworkOperation {
        return DirectoryNetworkOperation(DirectoryMapper(), DropboxDirectory(app))
    }
}