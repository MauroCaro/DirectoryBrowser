package com.base.directorybrowser.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.base.directorybrowser.dagger.injector.Injector
import com.base.directorybrowser.view.login.UserInformationUiModel
import javax.inject.Inject

open class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory
    var userInformation: UserInformationUiModel? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        Injector.component().inject(this)
    }
}
