package com.base.directorybrowser.dagger.module

import com.base.directorybrowser.view.directory.DirectoryUiMapper
import com.base.directorybrowser.view.directory.IDirectoryUiMapper
import com.base.directorybrowser.view.login.IUserInformationUiMapper
import com.base.directorybrowser.view.login.UserInformationUiMapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UiMapperModule {

    @Provides
    @Singleton
    internal fun provideUserInformationUiMapper(): IUserInformationUiMapper {
        return UserInformationUiMapper()
    }

    @Provides
    @Singleton
    internal fun provideDirectoryUiMapper(): IDirectoryUiMapper {
        return DirectoryUiMapper()
    }
}