package com.base.directorybrowser.dagger.module

import android.content.SharedPreferences
import com.base.directorybrowser.base.DirectoryBrowserApplication
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule(
    val app: DirectoryBrowserApplication,
    val pref: SharedPreferences,
    val key: String
) {
    @Provides
    @Singleton
    fun provideApplication() = app

    @Provides
    @Singleton
    fun provideSharedPreference(): SharedPreferences = pref

    @Provides
    @Singleton
    @Named("key")
    fun provideApiKey(): String {
        return key
    }
}