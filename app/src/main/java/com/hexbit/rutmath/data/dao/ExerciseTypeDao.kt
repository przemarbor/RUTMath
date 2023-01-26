package com.hexbit.rutmath.data.dao

import androidx.room.*
import com.hexbit.rutmath.data.model.ExerciseType
import com.hexbit.rutmath.data.model.Operation
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ExerciseTypeDao {
    @Query("SELECT * FROM ExerciseType WHERE userNick LIKE :nick")
    fun getAll(nick: String): Single<List<ExerciseType>>

    @Query("SELECT * FROM ExerciseType WHERE userNick LIKE :nick AND operation IN (:operations)")
    fun getAll(nick: String, operations: List<String>): Single<List<ExerciseType>>

    @Query("SELECT * FROM ExerciseType WHERE id LIKE :id")
    fun findById(id: String): Single<ExerciseType>

    @Query("SELECT * FROM ExerciseType WHERE userNick LIKE :nick AND operation LIKE :operation AND difficulty > :prevDifficulty ORDER BY difficulty LIMIT 1")
    fun findExerciseType(nick: String, operation:Operation, prevDifficulty: Int): Single<ExerciseType>

    @Insert
    fun insertAll(exerciseTypes: List<ExerciseType>): Completable

    @Delete
    fun delete(exerciseType: ExerciseType): Completable

    @Update
    fun update(exerciseType: ExerciseType): Completable
}