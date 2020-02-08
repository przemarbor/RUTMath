package com.hexbit.additionandsubtraction.util.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * ViewModel that have auto disposable mechanism.
 * Every disposable that should be disposed in end of ViewModel cycle of life should
 * be added in manageDisposable block of code.
 *
 * ViewModel z mechanizmem autoDisposable dla wszystkich obiektów Disposable które zostaną
 * stworzone w bloku kodu z użyciem manageDisposable()
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