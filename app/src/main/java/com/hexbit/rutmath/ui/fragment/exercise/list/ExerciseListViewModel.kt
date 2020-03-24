package com.hexbit.rutmath.ui.fragment.exercise.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hexbit.rutmath.data.AppDatabase
import com.hexbit.rutmath.data.model.ExerciseType
import com.hexbit.rutmath.data.model.Operation
import com.hexbit.rutmath.util.base.DisposableViewModel
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ExerciseListViewModel(private val database: AppDatabase) : DisposableViewModel() {

    private val exerciseTypes = MutableLiveData<List<ExerciseType>>()

    fun getExerciseTypes(): LiveData<List<ExerciseType>> = exerciseTypes

    /**
     * Load exercises from database.
     *
     * #// Metoda pobierająca listę z bazy danych i przekazująca ją do widoku..
     */
    fun loadExercises(nick: String) {
        manageDisposable {
            database.exerciseTypeDao().getAll(nick)
                .flatMap { list ->
                    if (list.isEmpty()) {
                        initializeExerciseListInDatabase(nick).andThen(
                            database.exerciseTypeDao().getAll(nick).subscribeOn(Schedulers.io())
                        )
                    } else {
                        Single.just(list)
                    }
                }
                .subscribeOn(Schedulers.io())
                .subscribe { list -> exerciseTypes.postValue(list) }
        }
    }

    private fun initializeExerciseListInDatabase(nick: String): Completable {
        val firstRange = 5..20 step 5
        val secondRange = 30..100 step 10
        val range = firstRange.plus(secondRange)
        val exercises = arrayListOf<ExerciseType>()

        range.forEach {
            exercises.add(
                ExerciseType(
                    Operation.PLUS,
                    it,
                    -1,
                    nick
                )
            )
            exercises.add(
                ExerciseType(
                    Operation.MINUS,
                    it,
                    -1,
                    nick
                )
            )
            exercises.add(
                ExerciseType(
                    Operation.PLUS_MINUS,
                    it,
                    -1,
                    nick
                )
            )
        }
        return database.exerciseTypeDao()
            .insertAll(exercises)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

    /**
     * It updates exercise type in database (for example when user finished game and we should update
     * exercise with new rate number)
     *
     * #// Metoda odświeżająca obiekt w bazie danych.
     */
    fun updateExerciseType(exerciseType: ExerciseType, nick: String) {
        manageDisposable {
            database.exerciseTypeDao()
                .update(exerciseType)
                .subscribeOn(Schedulers.io())
                .andThen(Single.defer {
                    database.exerciseTypeDao().getAll(nick)
                })
                .subscribe { newList ->
                    exerciseTypes.postValue(newList)
                }
        }
    }
}