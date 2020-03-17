package com.hexbit.additionandsubtraction.data.dao

import androidx.room.*
import com.hexbit.additionandsubtraction.data.model.ExerciseType
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ExerciseTypeDao {
    @Query("SELECT * FROM ExerciseType WHERE userNick LIKE :nick")
    fun getAll(nick: String): Single<List<ExerciseType>>

    @Query("SELECT * FROM ExerciseType WHERE id LIKE :id")
    fun findById(id: String): Single<ExerciseType>

    @Insert
    fun insertAll(exerciseTypes: List<ExerciseType>): Completable

    @Delete
    fun delete(exerciseType: ExerciseType): Completable

    @Update
    fun update(exerciseType: ExerciseType): Completable
}