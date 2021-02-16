package com.base.directorybrowser.base

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.base.core.dagger.NetworkOperationModule
import com.base.directorybrowser.R
import com.base.directorybrowser.dagger.component.ApplicationComponent
import com.base.directorybrowser.dagger.component.DaggerApplicationComponent
import com.base.directorybrowser.dagger.injector.Injector
import com.base.directorybrowser.dagger.module.AppModule

class DirectoryBrowserApplication : Application() {

   lateinit var prefs : SharedPreferences

    override fun onCreate() {
        super.onCreate()
        prefs = getSharedPreferences("dropbox-directory-browser", Context.MODE_PRIVATE)
        Injector.init(buildComponent())
        Injector.component().inject(this)
    }

    private fun buildComponent(): ApplicationComponent {
        return DaggerApplicationComponent.builder()
            .networkOperationModule(NetworkOperationModule(this, prefs))
            .appModule(AppModule(this, prefs, getString(R.string.dropbox_app_key)))
            .build()
    }
}