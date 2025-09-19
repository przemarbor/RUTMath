package com.octbit.rutmath.shared.data

import com.octbit.rutmath.shared.model.ExerciseType
import com.octbit.rutmath.shared.model.Player
import com.octbit.rutmath.shared.model.Score
import com.octbit.rutmath.shared.model.Settings

/**
 * Repository interface for database operations.
 * This abstraction allows different platforms to implement their own database solutions.
 */
interface DatabaseRepository {
    
    /**
     * Settings related operations
     */
    suspend fun getSettings(): Settings?
    suspend fun updateSettings(settings: Settings)
    suspend fun insertDefaultSettings(): Settings
    
    /**
     * Exercise type related operations
     */
    suspend fun getExerciseTypes(): List<ExerciseType>
    suspend fun getExerciseType(id: Int): ExerciseType?
    suspend fun updateExerciseType(exerciseType: ExerciseType)
    suspend fun insertExerciseType(exerciseType: ExerciseType): Int
    suspend fun deleteExerciseType(id: Int)
    
    /**
     * Score related operations
     */
    suspend fun saveScore(score: Score): Int
    suspend fun saveScores(scores: List<Score>)
    suspend fun getTopScores(limit: Int = 10): List<Score>
    suspend fun getAllScores(): List<Score>
    suspend fun deleteScore(id: Int)
    
    /**
     * Player related operations
     */
    suspend fun getPlayers(): List<Player>
    suspend fun getPlayer(id: Int): Player?
    suspend fun insertPlayer(player: Player): Int
    suspend fun updatePlayer(player: Player)
    suspend fun deletePlayer(id: Int)
}

/**
 * Local data source interface for caching and offline storage.
 */
interface LocalDataSource {
    suspend fun saveSettings(settings: Settings)
    suspend fun loadSettings(): Settings?
    suspend fun saveExerciseTypes(types: List<ExerciseType>)
    suspend fun loadExerciseTypes(): List<ExerciseType>
    suspend fun saveScores(scores: List<Score>)
    suspend fun loadScores(): List<Score>
    suspend fun clearCache()
} 