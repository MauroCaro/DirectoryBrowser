package com.base.directorybrowser.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel(){

    protected var compositeDisposable = CompositeDisposable()

    fun dispose() {
        compositeDisposable.dispose()
    }
    override fun onCleared() {
        dispose()
        super.onCleared()
    }
}