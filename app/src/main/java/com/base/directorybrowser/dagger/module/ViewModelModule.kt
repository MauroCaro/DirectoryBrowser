package com.base.directorybrowser.dagger.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.base.directorybrowser.dagger.injector.InjectorViewModelFactory
import com.base.directorybrowser.dagger.injector.ViewModelKey
import com.base.directorybrowser.view.directory.DirectoryViewModel
import com.base.directorybrowser.view.information.InformationDialogViewModel
import com.base.directorybrowser.view.login.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DirectoryViewModel::class)
    abstract fun bindDirectoryViewModel(viewModel: DirectoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InformationDialogViewModel::class)
    abstract fun bindInformationViewModel(viewModel: InformationDialogViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: InjectorViewModelFactory): ViewModelProvider.Factory
}