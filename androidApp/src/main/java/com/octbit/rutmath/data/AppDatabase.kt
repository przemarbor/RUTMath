package com.octbit.rutmath.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.octbit.rutmath.data.dao.ExerciseTypeDao
import com.octbit.rutmath.data.dao.ScoreDao
import com.octbit.rutmath.data.dao.SettingsDao
import com.octbit.rutmath.data.dao.UserDao
import com.octbit.rutmath.data.model.*

/**
 * Main abstraction for database. It contains whole Room database implementation.
 */

@Database(entities = [ExerciseType::class, Settings::class, Score::class, Player::class], version = 4, exportSchema = false)
@TypeConverters(OperationConverter::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_FILE = "rutmath.db"

        @Volatile
        private var instance: AppDatabase? = null
        private val lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_FILE
        ).build()
    }

    abstract fun exerciseTypeDao(): ExerciseTypeDao

    abstract fun settingsDao(): SettingsDao

    abstract fun scoreDao(): ScoreDao

    abstract fun userDao(): UserDao
}