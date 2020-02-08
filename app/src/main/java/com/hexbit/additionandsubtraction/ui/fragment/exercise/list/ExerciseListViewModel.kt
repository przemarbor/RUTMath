package com.hexbit.additionandsubtraction.ui.fragment.exercise.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hexbit.additionandsubtraction.data.AppDatabase
import com.hexbit.additionandsubtraction.data.model.ExerciseType
import com.hexbit.additionandsubtraction.data.model.Operation
import com.hexbit.additionandsubtraction.util.base.DisposableViewModel
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class ExerciseListViewModel(private val database: AppDatabase) : DisposableViewModel() {

    private val exerciseTypes = MutableLiveData<List<ExerciseType>>()

    fun getExerciseTypes(): LiveData<List<ExerciseType>> = exerciseTypes

    init {
        /**
         * Load exercises from database.
         *
         * Metoda pobierająca listę z bazy danych i przekazująca ją do widoku..
         */
        manageDisposable {
            database.exerciseTypeDao().getAll()
                .subscribeOn(Schedulers.io())
                .flatMap {
                    database.exerciseTypeDao().getAll()
                }.subscribe { list -> exerciseTypes.postValue(list) }
        }
    }

    /**
     * It updates exercise type in database (for example when user finished game and we should update
     * exercise with new rate number)
     *
     * Metoda odświeżająca obiekt w bazie danych.
     */
    fun updateExerciseType(exerciseType: ExerciseType) {
        manageDisposable {
            database.exerciseTypeDao()
                .update(exerciseType)
                .subscribeOn(Schedulers.io())
                .andThen(Single.defer {
                    database.exerciseTypeDao().getAll()
                })
                .subscribe { newList ->
                    exerciseTypes.postValue(newList)
                }
        }
    }
}