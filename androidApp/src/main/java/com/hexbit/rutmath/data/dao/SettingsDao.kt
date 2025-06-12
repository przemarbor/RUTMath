package com.hexbit.rutmath.data.dao

import androidx.room.*
import com.hexbit.rutmath.data.model.Settings
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface SettingsDao {
    @Query("SELECT * FROM Settings LIMIT 1")
    fun getAll(): Single<List<Settings>>

    @Query("SELECT * FROM Settings WHERE id LIKE :id")
    fun findById(id: String): Single<Settings>

    @Insert
    fun insertAll(exerciseTypes: List<Settings>): Completable

    @Delete
    fun delete(exerciseType: Settings): Completable

    @Update
    fun update(exerciseType: Settings): Completable
}