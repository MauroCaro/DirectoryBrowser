package com.base.directorybrowser.dagger.component

import com.base.core.dagger.NetworkOperationModule
import com.base.directorybrowser.base.BaseActivity
import com.base.directorybrowser.base.BaseViewModel
import com.base.directorybrowser.base.DirectoryBrowserApplication
import com.base.directorybrowser.dagger.module.AppModule
import com.base.directorybrowser.dagger.module.UiMapperModule
import com.base.directorybrowser.dagger.module.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, UiMapperModule::class, ViewModelModule::class, NetworkOperationModule::class])
interface ApplicationComponent {
    fun inject(baseActivity: BaseActivity)
    fun inject(activity: DirectoryBrowserApplication)
    fun inject(baseViewModel: BaseViewModel)
}