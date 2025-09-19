package com.octbit.rutmath.util.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * ViewModel that have auto disposable mechanism.
 * Every disposable that should be disposed in end of ViewModel cycle of life should
 * be added in manageDisposable block of code.
 *
 * ViewModel with autoDisposable mechanism for all Disposable objects that will be
 * created in the code block using manageDisposable()
 */
open class DisposableViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun manageDisposable(factory: () -> Disposable) {
        compositeDisposable.add(factory.invoke())
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}