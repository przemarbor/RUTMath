package com.octbit.rutmath.shared.data

import com.octbit.rutmath.shared.model.ExerciseType
import com.octbit.rutmath.shared.model.Player
import com.octbit.rutmath.shared.model.Score
import com.octbit.rutmath.shared.model.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Android-specific implementation of DatabaseRepository using Room database.
 * Uses Any for AppDatabase to avoid circular dependencies.
 */
class AndroidDatabaseRepository(
    private val database: Any // AppDatabase passed as Any to avoid circular dependency
) : DatabaseRepository {
    
    override suspend fun getSettings(): Settings? = withContext(Dispatchers.IO) {
        try {
            // Use reflection to access database methods
            val settingsDao = database::class.java.getMethod("settingsDao").invoke(database)
            val getAll = settingsDao::class.java.getMethod("getAll").invoke(settingsDao)
            val blockingGet = getAll::class.java.getMethod("blockingGet").invoke(getAll) as List<*>
            val roomSettings = blockingGet.firstOrNull()
            
            roomSettings?.let {
                val maxNumber = it::class.java.getMethod("getMaxNumberInBattleMode").invoke(it) as Int
                val nick1 = it::class.java.getMethod("getLastNickname1").invoke(it) as String
                val nick2 = it::class.java.getMethod("getLastNickname2").invoke(it) as String
                val language = it::class.java.getMethod("getLanguage").invoke(it) as String
                
                Settings(
                    maxNumberInBattleMode = maxNumber,
                    lastNickname1 = nick1,
                    lastNickname2 = nick2,
                    language = language
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    override suspend fun updateSettings(settings: Settings): Unit = withContext(Dispatchers.IO) {
        try {
            // Create Room Settings object using reflection
            val settingsClass = Class.forName("com.octbit.rutmath.data.model.Settings")
            val roomSettings = settingsClass.newInstance()
            
            // Set values
            settingsClass.getMethod("setMaxNumberInBattleMode", Int::class.java).invoke(roomSettings, settings.maxNumberInBattleMode)
            settingsClass.getMethod("setLastNickname1", String::class.java).invoke(roomSettings, settings.lastNickname1)
            settingsClass.getMethod("setLastNickname2", String::class.java).invoke(roomSettings, settings.lastNickname2)
            settingsClass.getMethod("setLanguage", String::class.java).invoke(roomSettings, settings.language)
            
            val settingsDao = database::class.java.getMethod("settingsDao").invoke(database)
            val getAll = settingsDao::class.java.getMethod("getAll").invoke(settingsDao)
            val blockingGet = getAll::class.java.getMethod("blockingGet").invoke(getAll) as List<*>
            val existingSettings = blockingGet.firstOrNull()
            
            if (existingSettings != null) {
                val id = existingSettings::class.java.getMethod("getId").invoke(existingSettings) as Int
                settingsClass.getMethod("setId", Int::class.java).invoke(roomSettings, id)
                val update = settingsDao::class.java.getMethod("update", settingsClass).invoke(settingsDao, roomSettings)
                update::class.java.getMethod("blockingGet").invoke(update)
            } else {
                val insertAll = settingsDao::class.java.getMethod("insertAll", List::class.java).invoke(settingsDao, listOf(roomSettings))
                insertAll::class.java.getMethod("blockingGet").invoke(insertAll)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    override suspend fun insertDefaultSettings(): Settings = withContext(Dispatchers.IO) {
        val defaultSettings = Settings()
        updateSettings(defaultSettings)
        defaultSettings
    }
    
    override suspend fun getExerciseTypes(): List<ExerciseType> = withContext(Dispatchers.IO) {
        try {
            val exerciseDao = database::class.java.getMethod("exerciseTypeDao").invoke(database)
            val getAll = exerciseDao::class.java.getMethod("getAll").invoke(exerciseDao)
            val blockingGet = getAll::class.java.getMethod("blockingGet").invoke(getAll) as List<*>
            
            blockingGet.map { roomExercise ->
                val operation = roomExercise!!::class.java.getMethod("getOperation").invoke(roomExercise)
                val difficulty = roomExercise::class.java.getMethod("getDifficulty").invoke(roomExercise) as Int
                val rate = roomExercise::class.java.getMethod("getRate").invoke(roomExercise) as Int
                val userNick = roomExercise::class.java.getMethod("getUserNick").invoke(roomExercise) as String
                val isUnlocked = roomExercise::class.java.getMethod("isUnlocked").invoke(roomExercise) as Boolean
                val id = roomExercise::class.java.getMethod("getId").invoke(roomExercise) as Int
                
                ExerciseType(
                    operation = convertRoomOperationByName(operation.toString()),
                    difficulty = difficulty,
                    rate = rate,
                    userNick = userNick,
                    isUnlocked = isUnlocked,
                    id = id
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    override suspend fun getExerciseType(id: Int): ExerciseType? = withContext(Dispatchers.IO) {
        try {
            val exerciseDao = database::class.java.getMethod("exerciseTypeDao").invoke(database)
            val getById = exerciseDao::class.java.getMethod("getById", Int::class.java).invoke(exerciseDao, id)
            val blockingGet = getById::class.java.getMethod("blockingGet").invoke(getById)
            
            blockingGet?.let { roomExercise ->
                val operation = roomExercise::class.java.getMethod("getOperation").invoke(roomExercise)
                val difficulty = roomExercise::class.java.getMethod("getDifficulty").invoke(roomExercise) as Int
                val rate = roomExercise::class.java.getMethod("getRate").invoke(roomExercise) as Int
                val userNick = roomExercise::class.java.getMethod("getUserNick").invoke(roomExercise) as String
                val isUnlocked = roomExercise::class.java.getMethod("isUnlocked").invoke(roomExercise) as Boolean
                val exerciseId = roomExercise::class.java.getMethod("getId").invoke(roomExercise) as Int
                
                ExerciseType(
                    operation = convertRoomOperationByName(operation.toString()),
                    difficulty = difficulty,
                    rate = rate,
                    userNick = userNick,
                    isUnlocked = isUnlocked,
                    id = exerciseId
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    override suspend fun updateExerciseType(exerciseType: ExerciseType) = withContext(Dispatchers.IO) {
        // Simplified implementation - just return for now
        // In production, implement full reflection-based update
    }
    
    override suspend fun insertExerciseType(exerciseType: ExerciseType): Int = withContext(Dispatchers.IO) {
        // Simplified implementation - return dummy ID
        0
    }
    
    override suspend fun deleteExerciseType(id: Int) = withContext(Dispatchers.IO) {
        // Simplified implementation 
    }
    
    override suspend fun saveScore(score: Score): Int = withContext(Dispatchers.IO) {
        // Simplified implementation - return dummy ID
        0
    }
    
    override suspend fun saveScores(scores: List<Score>) = withContext(Dispatchers.IO) {
        // Simplified implementation
    }
    
    override suspend fun getTopScores(limit: Int): List<Score> = withContext(Dispatchers.IO) {
        // Simplified implementation - return empty list
        emptyList()
    }
    
    override suspend fun getAllScores(): List<Score> = withContext(Dispatchers.IO) {
        // Simplified implementation - return empty list
        emptyList()
    }
    
    override suspend fun deleteScore(id: Int) = withContext(Dispatchers.IO) {
        // Simplified implementation
    }
    
    override suspend fun getPlayers(): List<Player> = withContext(Dispatchers.IO) {
        // Simplified implementation - return empty list
        emptyList()
    }
    
    override suspend fun getPlayer(id: Int): Player? = withContext(Dispatchers.IO) {
        // Simplified implementation - return null
        null
    }
    
    override suspend fun insertPlayer(player: Player): Int = withContext(Dispatchers.IO) {
        // Simplified implementation - return dummy ID
        0
    }
    
    override suspend fun updatePlayer(player: Player) = withContext(Dispatchers.IO) {
        // Simplified implementation
    }
    
    override suspend fun deletePlayer(id: Int) = withContext(Dispatchers.IO) {
        // Simplified implementation
    }
    
    /**
     * Converts Room Operation name to Shared Operation
     */
    private fun convertRoomOperationByName(operationName: String): com.octbit.rutmath.shared.model.Operation {
        return when (operationName) {
            "PLUS" -> com.octbit.rutmath.shared.model.Operation.PLUS
            "MINUS" -> com.octbit.rutmath.shared.model.Operation.MINUS
            "PLUS_MINUS" -> com.octbit.rutmath.shared.model.Operation.PLUS_MINUS
            "MULTIPLY" -> com.octbit.rutmath.shared.model.Operation.MULTIPLY
            "DIVIDE" -> com.octbit.rutmath.shared.model.Operation.DIVIDE
            "MULTIPLY_DIVIDE" -> com.octbit.rutmath.shared.model.Operation.MULTIPLY_DIVIDE
            "DIVISIBILITY" -> com.octbit.rutmath.shared.model.Operation.DIVISIBILITY
            "UNITS_TIME" -> com.octbit.rutmath.shared.model.Operation.UNITS_TIME
            "UNITS_LENGTH" -> com.octbit.rutmath.shared.model.Operation.UNITS_LENGTH
            "UNITS_WEIGHT" -> com.octbit.rutmath.shared.model.Operation.UNITS_WEIGHT
            "UNITS_SURFACE" -> com.octbit.rutmath.shared.model.Operation.UNITS_SURFACE
            "UNITS_ALL" -> com.octbit.rutmath.shared.model.Operation.UNITS_ALL
            "NEGATIVE_PLUS" -> com.octbit.rutmath.shared.model.Operation.NEGATIVE_PLUS
            "NEGATIVE_MINUS" -> com.octbit.rutmath.shared.model.Operation.NEGATIVE_MINUS
            "NEGATIVE_PLUS_MINUS" -> com.octbit.rutmath.shared.model.Operation.NEGATIVE_PLUS_MINUS
            "NEGATIVE_MUL" -> com.octbit.rutmath.shared.model.Operation.NEGATIVE_MUL
            "NEGATIVE_DIV" -> com.octbit.rutmath.shared.model.Operation.NEGATIVE_DIV
            "NEGATIVE_MUL_DIV" -> com.octbit.rutmath.shared.model.Operation.NEGATIVE_MUL_DIV
            else -> com.octbit.rutmath.shared.model.Operation.PLUS
        }
    }
} 
