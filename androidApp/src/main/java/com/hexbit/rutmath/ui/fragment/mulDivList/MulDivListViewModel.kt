package com.octbit.rutmath.ui.fragment.mulDivList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.octbit.rutmath.data.AppDatabase
import com.octbit.rutmath.data.model.ExerciseType
import com.octbit.rutmath.data.model.Operation
import com.octbit.rutmath.util.base.DisposableViewModel
import io.reactivex.Single
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
            database.exerciseTypeDao()
                .getAll(
                    nick,
                    listOf(
                        "MULTIPLY",
                        "DIVIDE",
                        "MULTIPLY_DIVIDE",
                        "NEGATIVE_MUL",
                        "NEGATIVE_DIV",
                        "NEGATIVE_MUL_DIV"
                    )
                )
                .flatMap { existingList ->
                    val allExercises = generateAllExercises(nick) // Generate all exercises as a list
                    val missingExercises = allExercises.filter { newExercise ->
                        existingList.none {
                            it.operation == newExercise.operation && it.difficulty == newExercise.difficulty
                        }
                    }

                    if (missingExercises.isNotEmpty()) {
                        database.exerciseTypeDao()
                            .insertAll(missingExercises)
                            .andThen(Single.just(existingList + missingExercises)) // Combine updated list
                    } else {
                        Single.just(existingList) // No new exercises to add
                    }
                }
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { updatedList -> exerciseTypes.postValue(updatedList) },
                    { println("ERROR: Cannot load or update Exercises") }
                )
        }
    }


    private fun generateAllExercises(nick: String): List<ExerciseType> {
        val exercises = mutableListOf<ExerciseType>()
        val range = (10..20 step 5) + (30..60 step 10) + (80..100 step 20) + 200

        // Add unlocked MULTIPLY/DIVIDE exercises
        exercises.add(ExerciseType(Operation.MULTIPLY, 5, -1, nick, true))
        exercises.add(ExerciseType(Operation.DIVIDE, 5, -1, nick, true))
        exercises.add(ExerciseType(Operation.MULTIPLY_DIVIDE, 5, -1, nick, true))

        // Add locked MULTIPLY/DIVIDE exercises
        range.forEach {
            exercises.add(ExerciseType(Operation.MULTIPLY, it, -1, nick, false))
            exercises.add(ExerciseType(Operation.DIVIDE, it, -1, nick, false))
            exercises.add(ExerciseType(Operation.MULTIPLY_DIVIDE, it, -1, nick, false))
        }

        // Add NEGATIVE_MUL/DIV exercises
        exercises.add(ExerciseType(Operation.NEGATIVE_MUL, 5, -1, nick, false))
        exercises.add(ExerciseType(Operation.NEGATIVE_DIV, 5, -1, nick, false))
        exercises.add(ExerciseType(Operation.NEGATIVE_MUL_DIV, 5, -1, nick, false))

        range.forEach {
            exercises.add(ExerciseType(Operation.NEGATIVE_MUL, it, -1, nick, false))
            exercises.add(ExerciseType(Operation.NEGATIVE_DIV, it, -1, nick, false))
            exercises.add(ExerciseType(Operation.NEGATIVE_MUL_DIV, it, -1, nick, false))
        }

        return exercises
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
                    database.exerciseTypeDao().getAll(nick, listOf("MULTIPLY","DIVIDE","MULTIPLY_DIVIDE","NEGATIVE_MUL","NEGATIVE_DIV","NEGATIVE_MUL_DIV"))
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
