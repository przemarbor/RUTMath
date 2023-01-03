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
     * Load exercises from database and pass it to view.
     *
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
    /**
     *  Initialize exercises in Database for the New Player
     */
    private fun initializeExerciseListInDatabase(nick: String): Completable {
        val exercises = arrayListOf<ExerciseType>()
        val rangePlusMinusLocked = (10..20 step 5) + (30..100 step 10)
        val rangeMultDivLocked = 20..100 step 10

        /**
         *  Add unlocked PLUS/MINUS exercises
         */
        exercises.add(
            ExerciseType(
                Operation.PLUS,
                5,
                -1,
                nick,
                true
            )
        )
        exercises.add(
            ExerciseType(
                Operation.MINUS,
                5,
                -1,
                nick,
                true
            )
        )
        exercises.add(
            ExerciseType(
                Operation.PLUS_MINUS,
                5,
                -1,
                nick,
                true
            )
        )
        /**
         *  Add locked PLUS/MINUS exercises
         */
        rangePlusMinusLocked.forEach {
            exercises.add(
                ExerciseType(
                    Operation.PLUS,
                    it,
                    -1,
                    nick,
                    false
                )
            )
            exercises.add(
                ExerciseType(
                    Operation.MINUS,
                    it,
                    -1,
                    nick,
                    false
                )
            )
            exercises.add(
                ExerciseType(
                    Operation.PLUS_MINUS,
                    it,
                    -1,
                    nick,
                    false
                )
            )
        }
        /**
         *  Add unlocked MULTIPLY/DIVIDE exercises
         */
        exercises.add(
            ExerciseType(
                Operation.MULTIPLY,
                10,
                -1,
                nick,
                true
            )
        )
        exercises.add(
            ExerciseType(
                Operation.DIVIDE,
                10,
                -1,
                nick,
                true
            )
        )
        exercises.add(
            ExerciseType(
                Operation.MULTIPLY_DIVIDE,
                10,
                -1,
                nick,
                true
            )
        )
        /**
         *  Add locked MULTIPLY/DIVIDE exercises
         */
        rangeMultDivLocked.forEach {
            exercises.add(
                ExerciseType(
                    Operation.MULTIPLY,
                    it,
                    -1,
                    nick,
                    false
                )
            )
            exercises.add(
                ExerciseType(
                    Operation.DIVIDE,
                    it,
                    -1,
                    nick,
                    false
                )
            )
            exercises.add(
                ExerciseType(
                    Operation.MULTIPLY_DIVIDE,
                    it,
                    -1,
                    nick,
                    false
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
    /**
     *  Unlocks next exercise (example use: the player has completed an exercise therefore the next exercise is unlocked)
     *  updateExerciseType function required!
     */
    fun unlockExerciseType(id: Int, nick: String) {
        manageDisposable {
            database.exerciseTypeDao().findById(id.toString())
                .flatMap { exerciseType ->
                    println(exerciseType)
                        Single.just(exerciseType)
                }
                .subscribeOn(Schedulers.io())
                .subscribe ({ exerciseType -> updateExerciseType(exerciseType.copy(unlocked = true), nick) },
                { println("Cannot find ExerciseType to unlock") })
        }
    }
}