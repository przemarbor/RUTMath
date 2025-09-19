package com.octbit.rutmath.shared.usecase

import com.octbit.rutmath.shared.data.DatabaseRepository
import com.octbit.rutmath.shared.model.ExerciseType
import com.octbit.rutmath.shared.model.Player
import com.octbit.rutmath.shared.model.Score
import com.octbit.rutmath.shared.model.Settings

/**
 * Use case for managing data operations.
 * This class encapsulates business logic related to data management.
 */
class DataUseCase(
    private val databaseRepository: DatabaseRepository
) {
    
    /**
     * Gets application settings, creating default if none exist.
     */
    suspend fun getSettings(): Settings {
        return databaseRepository.getSettings() ?: databaseRepository.insertDefaultSettings()
    }
    
    /**
     * Updates application settings.
     */
    suspend fun updateSettings(settings: Settings) {
        databaseRepository.updateSettings(settings)
    }
    
    /**
     * Gets all exercise types.
     */
    suspend fun getExerciseTypes(): List<ExerciseType> {
        return databaseRepository.getExerciseTypes()
    }
    
    /**
     * Updates an exercise type with new progress/rate.
     */
    suspend fun updateExerciseProgress(exerciseType: ExerciseType, newRate: Int): ExerciseType {
        val updatedExerciseType = exerciseType.copy(rate = newRate)
        databaseRepository.updateExerciseType(updatedExerciseType)
        return updatedExerciseType
    }
    
    /**
     * Unlocks the next exercise type if conditions are met.
     */
    suspend fun checkAndUnlockNextExercise(completedExercise: ExerciseType, achievedRate: Int) {
        if (achievedRate >= 3) { // 3+ stars unlocks next exercise
            val allExercises = databaseRepository.getExerciseTypes()
            val nextExercise = allExercises.find { 
                it.operation == completedExercise.operation && 
                it.difficulty == completedExercise.difficulty + 1 &&
                !it.isUnlocked
            }
            
            nextExercise?.let {
                val unlockedExercise = it.copy(isUnlocked = true)
                databaseRepository.updateExerciseType(unlockedExercise)
            }
        }
    }
    
    /**
     * Saves game score(s) and returns the saved record(s).
     */
    suspend fun saveGameScore(score: Score): Score {
        val savedId = databaseRepository.saveScore(score)
        return score.copy(id = savedId)
    }
    
    /**
     * Saves multiple scores (for battle mode draws).
     */
    suspend fun saveGameScores(scores: List<Score>) {
        databaseRepository.saveScores(scores)
    }
    
    /**
     * Gets top scores for leaderboard.
     */
    suspend fun getTopScores(limit: Int = 10): List<Score> {
        return databaseRepository.getTopScores(limit)
    }
    
    /**
     * Gets or creates a player.
     */
    suspend fun getOrCreatePlayer(nickname: String): Player {
        val existingPlayers = databaseRepository.getPlayers()
        val existingPlayer = existingPlayers.find { it.nickname == nickname }
        
        return existingPlayer ?: run {
            val newPlayer = Player(nickname = nickname)
            val savedId = databaseRepository.insertPlayer(newPlayer)
            newPlayer.copy(id = savedId)
        }
    }
    
    /**
     * Updates player information.
     */
    suspend fun updatePlayer(player: Player) {
        databaseRepository.updatePlayer(player)
    }
    
    /**
     * Gets all players.
     */
    suspend fun getAllPlayers(): List<Player> {
        return databaseRepository.getPlayers()
    }
    
    /**
     * Initializes default exercise types if database is empty.
     */
    suspend fun initializeDefaultExercisesIfNeeded() {
        val existingExercises = databaseRepository.getExerciseTypes()
        if (existingExercises.isEmpty()) {
            // Initialize with default exercises - first few unlocked
            val defaultExercises = createDefaultExerciseTypes()
            defaultExercises.forEach { exercise ->
                databaseRepository.insertExerciseType(exercise)
            }
        }
    }
    
    /**
     * Creates default exercise types for a new installation.
     */
    private fun createDefaultExerciseTypes(): List<ExerciseType> {
        return listOf(
            // Basic addition - unlocked by default
            ExerciseType(
                operation = com.octbit.rutmath.shared.model.Operation.PLUS,
                difficulty = 10,
                rate = 0,
                userNick = "Player 1",
                isUnlocked = true
            ),
            // Basic subtraction - unlocked by default
            ExerciseType(
                operation = com.octbit.rutmath.shared.model.Operation.MINUS,
                difficulty = 10,
                rate = 0,
                userNick = "Player 1",
                isUnlocked = true
            ),
            // Mixed addition/subtraction - locked initially
            ExerciseType(
                operation = com.octbit.rutmath.shared.model.Operation.PLUS_MINUS,
                difficulty = 15,
                rate = 0,
                userNick = "Player 1",
                isUnlocked = false
            ),
            // Basic multiplication - locked initially
            ExerciseType(
                operation = com.octbit.rutmath.shared.model.Operation.MULTIPLY,
                difficulty = 20,
                rate = 0,
                userNick = "Player 1",
                isUnlocked = false
            )
            // Add more as needed...
        )
    }
} 
