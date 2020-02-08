package com.hexbit.additionandsubtraction.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hexbit.additionandsubtraction.data.dao.ExerciseTypeDao
import com.hexbit.additionandsubtraction.data.dao.ScoreDao
import com.hexbit.additionandsubtraction.data.dao.SettingsDao
import com.hexbit.additionandsubtraction.data.model.ExerciseType
import com.hexbit.additionandsubtraction.data.model.OperationConverter
import com.hexbit.additionandsubtraction.data.model.Score
import com.hexbit.additionandsubtraction.data.model.Settings

/**
 * Main abstraction for database. It contains whole Room database implementation.
 *
 * Główna klasa bazy danych z implementacją jako singleton.
 */
@Database(entities = [ExerciseType::class, Settings::class, Score::class], version = 3)
@TypeConverters(OperationConverter::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_FILE = "additionandsubtracionapp.db"

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
}