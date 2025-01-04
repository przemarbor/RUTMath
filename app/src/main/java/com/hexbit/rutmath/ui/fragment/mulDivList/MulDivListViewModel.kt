package com.hexbit.rutmath.ui.fragment.mulDivList

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

class MulDivListViewModel(private val database: AppDatabase) : DisposableViewModel() {

    private val exerciseTypes = MutableLiveData<List<ExerciseType>>()
    fun getExerciseTypes(): LiveData<List<ExerciseType>> = exerciseTypes

    /**
     * Load exercises from database and pass it to view.
     *
     */
    fun loadExercises(nick: String) {
        manageDisposable {
            database.exerciseTypeDao().getAll(nick, listOf("MULTIPLY","DIVIDE","MULTIPLY_DIVIDE","NEGATIVE_PLUS_MUL","NEGATIVE_MINUS_MUL","NEGATIVE_PLUS_DIV","NEGATIVE_MINUS_DIV","NEGATIVE_PLUS_MINUS_MUL","NEGATIVE_PLUS_MINUS_DIV"))
                .flatMap { list ->
                    if (list.isEmpty()) {
                        initializeExerciseListInDatabase(nick).andThen(
                            database.exerciseTypeDao().getAll(nick, listOf("MULTIPLY","DIVIDE","MULTIPLY_DIVIDE","NEGATIVE_PLUS_MUL","NEGATIVE_MINUS_MUL","NEGATIVE_PLUS_DIV","NEGATIVE_MINUS_DIV","NEGATIVE_PLUS_MINUS_MUL","NEGATIVE_PLUS_MINUS_DIV")).subscribeOn(Schedulers.io())
                        )
                    } else {
                        Single.just(list)
                    }
                }
                .subscribeOn(Schedulers.io())
                .subscribe( { list -> exerciseTypes.postValue(list) },
                    { println("ERROR: Cannot load Exercises") })
        }
    }
    /**
     *  Initialize exercises in Database for the New Player
     */
    private fun initializeExerciseListInDatabase(nick: String): Completable {
        val exercises = arrayListOf<ExerciseType>()
        //val range = (20..100 step 10) + 200
        val range = (10..20 step 5) + (30..60 step 10) + (80..100 step 20) +200
        /**
         *  Add unlocked MULTIPLY/DIVIDE exercises
         */
        exercises.add(
            ExerciseType(
                Operation.MULTIPLY,
                5,
                -1,
                nick,
                true
            )
        )
        exercises.add(
            ExerciseType(
                Operation.DIVIDE,
                5,
                -1,
                nick,
                true
            )
        )
        exercises.add(
            ExerciseType(
                Operation.MULTIPLY_DIVIDE,
                5,
                -1,
                nick,
                true
            )
        )
        /*exercises.add(
            ExerciseType(
                Operation.NEGATIVE_MINUS_MUL,
                5,
                -1,
                nick,
                true
            )
        )
        exercises.add(
            ExerciseType(
                Operation.NEGATIVE_PLUS_MUL,
                5,
                -1,
                nick,
                true
            )
        )
        exercises.add(
            ExerciseType(
                Operation.NEGATIVE_PLUS_DIV,
                5,
                -1,
                nick,
                true
            )
        )
        exercises.add(
            ExerciseType(
                Operation.NEGATIVE_MINUS_DIV,
                5,
                -1,
                nick,
                true
            )
        ) */







        /**
         *  Add locked MULTIPLY/DIVIDE exercises
         */
        range.forEach {
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
        // Negative Multiplication
        exercises.add(
            ExerciseType(
                Operation.NEGATIVE_PLUS_MUL,
                5,
                -1,
                nick,
                false
            )
        )
        exercises.add(
            ExerciseType(
                Operation.NEGATIVE_MINUS_MUL,
                5,
                -1,
                nick,
                false
            )
        )
        exercises.add(
            ExerciseType(
                Operation.NEGATIVE_PLUS_MINUS_MUL,
                5,
                -1,
                nick,
                false
            )
        )

        range.forEach {
            // Negative Multiplication
            exercises.add(
                ExerciseType(
                    Operation.NEGATIVE_PLUS_MUL,
                    it,
                    -1,
                    nick,
                    false
                )
            )
            exercises.add(
                ExerciseType(
                    Operation.NEGATIVE_MINUS_MUL,
                    it,
                    -1,
                    nick,
                    false
                )
            )
            exercises.add(
                ExerciseType(
                    Operation.NEGATIVE_PLUS_MINUS_MUL,
                    it,
                    -1,
                    nick,
                    false
                )
            )

        }

        // Negative Division
        exercises.add(
            ExerciseType(
                Operation.NEGATIVE_PLUS_DIV,
                5,
                -1,
                nick,
                false
            )
        )
        exercises.add(
            ExerciseType(
                Operation.NEGATIVE_MINUS_DIV,
                5,
                -1,
                nick,
                false
            )
        )
        exercises.add(
            ExerciseType(
                Operation.NEGATIVE_PLUS_MINUS_DIV,
                5,
                -1,
                nick,
                false
            )
        )

        range.forEach {
            // Negative Division
            exercises.add(
                ExerciseType(
                    Operation.NEGATIVE_PLUS_DIV,
                    it,
                    -1,
                    nick,
                    false
                )
            )
            exercises.add(
                ExerciseType(
                    Operation.NEGATIVE_MINUS_DIV,
                    it,
                    -1,
                    nick,
                    false
                )
            )

            exercises.add(
                ExerciseType(
                    Operation.NEGATIVE_PLUS_MINUS_DIV,
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
                    database.exerciseTypeDao().getAll(nick, listOf("MULTIPLY","DIVIDE","MULTIPLY_DIVIDE","NEGATIVE_PLUS_MUL","NEGATIVE_MINUS_MUL","NEGATIVE_PLUS_DIV","NEGATIVE_MINUS_DIV","NEGATIVE_PLUS_MINUS_MUL","NEGATIVE_PLUS_MINUS_DIV"))
                })
                .subscribe ({ newList ->
                    exerciseTypes.postValue(newList)
                },
                    { println("ERROR: Cannot update Exercises") })
        }
    }
    /**
     *  Unlocks exercise (example use: the player has completed an exercise therefore the next exercise is unlocked)
     *  updateExerciseType function required!
     *
     *  @param nick Player's Nickname.
     *  @param operation Type of ExerciseType's Operation to unlock.
     *  @param prevDifficulty The difficulty of the previous ExerciseType
     */
    fun unlockExerciseType(nick: String, operation: Operation, prevDifficulty: Int) {
        manageDisposable {
            database.exerciseTypeDao().findExerciseType(nick, operation, prevDifficulty)
                .flatMap { exerciseType ->
                    Single.just(exerciseType)
                }
                .subscribeOn(Schedulers.io())
                .subscribe ({ exerciseType -> updateExerciseType(exerciseType.copy(isUnlocked = true), nick) },
                    { println("ERROR: Cannot find ExerciseType to unlock") })
        }
    }
}