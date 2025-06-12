package com.hexbit.rutmath.shared.data

import com.hexbit.rutmath.shared.model.ExerciseType
import com.hexbit.rutmath.shared.model.Player
import com.hexbit.rutmath.shared.model.Score
import com.hexbit.rutmath.shared.model.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import platform.Foundation.NSUserDefaults

/**
 * iOS-specific implementation of DatabaseRepository using UserDefaults.
 * This is a basic implementation - in production you might want to use CoreData or SQLite.
 */
class IosDatabaseRepository : DatabaseRepository {
    
    private val userDefaults = NSUserDefaults.standardUserDefaults
    private val json = Json { ignoreUnknownKeys = true }
    
    companion object {
        private const val SETTINGS_KEY = "app_settings"
        private const val EXERCISE_TYPES_KEY = "exercise_types"
        private const val SCORES_KEY = "scores"
        private const val PLAYERS_KEY = "players"
        private const val NEXT_ID_KEY = "next_id"
    }
    
    private fun getNextId(): Int {
        val currentId = userDefaults.integerForKey(NEXT_ID_KEY).toInt()
        userDefaults.setInteger((currentId + 1).toLong(), NEXT_ID_KEY)
        return currentId + 1
    }
    
    override suspend fun getSettings(): Settings? = withContext(Dispatchers.Main) {
        val settingsString = userDefaults.stringForKey(SETTINGS_KEY)
        settingsString?.let {
            try {
                json.decodeFromString<Settings>(it)
            } catch (e: Exception) {
                null
            }
        }
    }
    
    override suspend fun updateSettings(settings: Settings) = withContext(Dispatchers.Main) {
        val settingsString = json.encodeToString(settings)
        userDefaults.setObject(settingsString, SETTINGS_KEY)
    }
    
    override suspend fun insertDefaultSettings(): Settings = withContext(Dispatchers.Main) {
        val defaultSettings = Settings()
        updateSettings(defaultSettings)
        defaultSettings
    }
    
    override suspend fun getExerciseTypes(): List<ExerciseType> = withContext(Dispatchers.Main) {
        val exerciseTypesString = userDefaults.stringForKey(EXERCISE_TYPES_KEY)
        exerciseTypesString?.let {
            try {
                json.decodeFromString<List<ExerciseType>>(it)
            } catch (e: Exception) {
                emptyList()
            }
        } ?: emptyList()
    }
    
    override suspend fun getExerciseType(id: Int): ExerciseType? = withContext(Dispatchers.Main) {
        getExerciseTypes().find { it.id == id }
    }
    
    override suspend fun updateExerciseType(exerciseType: ExerciseType) = withContext(Dispatchers.Main) {
        val exerciseTypes = getExerciseTypes().toMutableList()
        val index = exerciseTypes.indexOfFirst { it.id == exerciseType.id }
        if (index != -1) {
            exerciseTypes[index] = exerciseType
            val exerciseTypesString = json.encodeToString(exerciseTypes)
            userDefaults.setObject(exerciseTypesString, EXERCISE_TYPES_KEY)
        }
    }
    
    override suspend fun insertExerciseType(exerciseType: ExerciseType): Int = withContext(Dispatchers.Main) {
        val exerciseTypes = getExerciseTypes().toMutableList()
        val newId = getNextId()
        val newExerciseType = exerciseType.copy(id = newId)
        exerciseTypes.add(newExerciseType)
        val exerciseTypesString = json.encodeToString(exerciseTypes)
        userDefaults.setObject(exerciseTypesString, EXERCISE_TYPES_KEY)
        newId
    }
    
    override suspend fun deleteExerciseType(id: Int) = withContext(Dispatchers.Main) {
        val exerciseTypes = getExerciseTypes().toMutableList()
        exerciseTypes.removeAll { it.id == id }
        val exerciseTypesString = json.encodeToString(exerciseTypes)
        userDefaults.setObject(exerciseTypesString, EXERCISE_TYPES_KEY)
    }
    
    override suspend fun saveScore(score: Score): Int = withContext(Dispatchers.Main) {
        val scores = getAllScores().toMutableList()
        val newId = getNextId()
        val newScore = score.copy(id = newId)
        scores.add(newScore)
        val scoresString = json.encodeToString(scores)
        userDefaults.setObject(scoresString, SCORES_KEY)
        newId
    }
    
    override suspend fun saveScores(scores: List<Score>) = withContext(Dispatchers.Main) {
        val existingScores = getAllScores().toMutableList()
        scores.forEach { score ->
            val newId = getNextId()
            val newScore = score.copy(id = newId)
            existingScores.add(newScore)
        }
        val scoresString = json.encodeToString(existingScores)
        userDefaults.setObject(scoresString, SCORES_KEY)
    }
    
    override suspend fun getTopScores(limit: Int): List<Score> = withContext(Dispatchers.Main) {
        getAllScores().sortedByDescending { it.score }.take(limit)
    }
    
    override suspend fun getAllScores(): List<Score> = withContext(Dispatchers.Main) {
        val scoresString = userDefaults.stringForKey(SCORES_KEY)
        scoresString?.let {
            try {
                json.decodeFromString<List<Score>>(it)
            } catch (e: Exception) {
                emptyList()
            }
        } ?: emptyList()
    }
    
    override suspend fun deleteScore(id: Int) = withContext(Dispatchers.Main) {
        val scores = getAllScores().toMutableList()
        scores.removeAll { it.id == id }
        val scoresString = json.encodeToString(scores)
        userDefaults.setObject(scoresString, SCORES_KEY)
    }
    
    override suspend fun getPlayers(): List<Player> = withContext(Dispatchers.Main) {
        val playersString = userDefaults.stringForKey(PLAYERS_KEY)
        playersString?.let {
            try {
                json.decodeFromString<List<Player>>(it)
            } catch (e: Exception) {
                emptyList()
            }
        } ?: emptyList()
    }
    
    override suspend fun getPlayer(id: Int): Player? = withContext(Dispatchers.Main) {
        getPlayers().find { it.id == id }
    }
    
    override suspend fun insertPlayer(player: Player): Int = withContext(Dispatchers.Main) {
        val players = getPlayers().toMutableList()
        val newId = getNextId()
        val newPlayer = player.copy(id = newId)
        players.add(newPlayer)
        val playersString = json.encodeToString(players)
        userDefaults.setObject(playersString, PLAYERS_KEY)
        newId
    }
    
    override suspend fun updatePlayer(player: Player) = withContext(Dispatchers.Main) {
        val players = getPlayers().toMutableList()
        val index = players.indexOfFirst { it.id == player.id }
        if (index != -1) {
            players[index] = player
            val playersString = json.encodeToString(players)
            userDefaults.setObject(playersString, PLAYERS_KEY)
        }
    }
    
    override suspend fun deletePlayer(id: Int) = withContext(Dispatchers.Main) {
        val players = getPlayers().toMutableList()
        players.removeAll { it.id == id }
        val playersString = json.encodeToString(players)
        userDefaults.setObject(playersString, PLAYERS_KEY)
    }
} 