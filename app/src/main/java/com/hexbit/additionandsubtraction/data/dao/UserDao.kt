package com.hexbit.additionandsubtraction.data.dao

import androidx.room.*
import com.hexbit.additionandsubtraction.data.model.Player
import com.hexbit.additionandsubtraction.data.model.PlayerWithExercises
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface UserDao {
    @Query("SELECT * FROM Player")
    fun getAll(): Single<List<Player>>

    @Query("SELECT * FROM Player WHERE nick LIKE :nick")
    fun findByNick(nick: String): Single<Player>

    @Insert
    fun insert(player: Player): Completable

    @Transaction
    @Query("SELECT * FROM Player WHERE nick LIKE :nick")
    fun getPlayerWithScores(nick: String): Single<List<PlayerWithExercises>>

    @Delete
    fun delete(player: Player): Completable

    @Update
    fun update(player: Player): Completable
}