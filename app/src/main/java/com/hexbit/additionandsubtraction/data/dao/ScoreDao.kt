package com.hexbit.additionandsubtraction.data.dao

import androidx.room.*
import com.hexbit.additionandsubtraction.data.model.Score
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ScoreDao {
    @Query("SELECT * FROM Score")
    fun getAll(): Single<List<Score>>

    @Query("SELECT * FROM Score WHERE id LIKE :id")
    fun findById(id: String): Single<Score>

    @Insert
    fun insertAll(scores: List<Score>): Completable

    @Insert
    fun insert(score: Score): Completable

    @Delete
    fun delete(score: Score): Completable

    @Update
    fun update(score: Score): Completable
}