package com.hexbit.rutmath.shared.api

import com.hexbit.rutmath.shared.data.DatabaseRepository
import com.hexbit.rutmath.shared.data.IosDatabaseRepository
import com.hexbit.rutmath.shared.di.iosSharedModule
import com.hexbit.rutmath.shared.di.sharedModule
import com.hexbit.rutmath.shared.game.EquationGenerator
import com.hexbit.rutmath.shared.model.*
import com.hexbit.rutmath.shared.usecase.DataUseCase
import com.hexbit.rutmath.shared.usecase.GameUseCase
import com.hexbit.rutmath.shared.usecase.UnitsGameUseCase
import kotlinx.coroutines.*
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Main iOS API class that provides access to shared functionality.
 * This class should be used from iOS/Swift code to interact with shared business logic.
 */
class IosSharedApi : KoinComponent {
    
    private val dataUseCase: DataUseCase by inject()
    private val gameUseCase: GameUseCase by inject()
    private val unitsGameUseCase: UnitsGameUseCase by inject()
    private val equationGenerator: EquationGenerator by inject()
    
    companion object {
        fun initialize() {
            startKoin {
                modules(sharedModule, iosSharedModule)
            }
        }
        
        fun cleanup() {
            stopKoin()
        }
    }
    
    // MARK: - Data Operations
    
    /**
     * Gets application settings.
     */
    fun getSettings(completion: (Settings?, String?) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val settings = dataUseCase.getSettings()
                completion(settings, null)
            } catch (e: Exception) {
                completion(null, e.message)
            }
        }
    }
    
    /**
     * Updates application settings.
     */
    fun updateSettings(settings: Settings, completion: (String?) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                dataUseCase.updateSettings(settings)
                completion(null)
            } catch (e: Exception) {
                completion(e.message)
            }
        }
    }
    
    /**
     * Gets all exercise types.
     */
    fun getExerciseTypes(completion: (List<ExerciseType>?, String?) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val exerciseTypes = dataUseCase.getExerciseTypes()
                completion(exerciseTypes, null)
            } catch (e: Exception) {
                completion(null, e.message)
            }
        }
    }
    
    /**
     * Initializes default exercises if needed.
     */
    fun initializeDefaultExercises(completion: (String?) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                dataUseCase.initializeDefaultExercisesIfNeeded()
                completion(null)
            } catch (e: Exception) {
                completion(e.message)
            }
        }
    }
    
    /**
     * Saves a game score.
     */
    fun saveScore(score: Score, completion: (Score?, String?) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val savedScore = dataUseCase.saveGameScore(score)
                completion(savedScore, null)
            } catch (e: Exception) {
                completion(null, e.message)
            }
        }
    }
    
    /**
     * Gets top scores.
     */
    fun getTopScores(limit: Int = 10, completion: (List<Score>?, String?) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val scores = dataUseCase.getTopScores(limit)
                completion(scores, null)
            } catch (e: Exception) {
                completion(null, e.message)
            }
        }
    }
    
    // MARK: - Game Operations
    
    /**
     * Generates a single equation for battle mode or quick games.
     */
    fun generateEquation(operation: Operation, difficulty: Int, completion: (Equation?, String?) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val equation = equationGenerator.generateEquation(operation, difficulty)
                completion(equation, null)
            } catch (e: Exception) {
                completion(null, e.message)
            }
        }
    }
    
    /**
     * Generates answer choices for multiple choice questions.
     */
    fun generateAnswerChoices(correctAnswer: Int, count: Int = 4, completion: (List<Int>?, String?) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val answers = equationGenerator.generateAnswers(correctAnswer, count)
                completion(answers, null)
            } catch (e: Exception) {
                completion(null, e.message)
            }
        }
    }
    
    /**
     * Validates an answer for an equation.
     */
    fun validateAnswer(equation: Equation, answer: Int): Boolean {
        return equation.correctAnswer == answer
    }
    
    // MARK: - Units Game Operations
    
    /**
     * Generates a units equation.
     */
    fun generateUnitsEquation(operation: Operation, difficulty: Int, completion: (EquationUnits?, String?) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val equation = equationGenerator.generateUnitsEquation(operation, difficulty)
                completion(equation, null)
            } catch (e: Exception) {
                completion(null, e.message)
            }
        }
    }
    
    /**
     * Gets help text for units operations.
     */
    fun getUnitsHelpText(operation: Operation, completion: (String?, String?) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val helpText = unitsGameUseCase.createHelpText(operation)
                completion(helpText, null)
            } catch (e: Exception) {
                completion(null, e.message)
            }
        }
    }
    
    /**
     * Gets unit name for given operation and unit ID.
     */
    fun getUnitName(operation: Operation, unitId: Int, completion: (String?, String?) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val unitName = unitsGameUseCase.getUnitName(operation, unitId)
                completion(unitName, null)
            } catch (e: Exception) {
                completion(null, e.message)
            }
        }
    }
    
    /**
     * Validates a units equation answer.
     */
    fun validateUnitsAnswer(equation: EquationUnits, answer: Int): Boolean {
        return equation.correctAnswer == answer
    }
} 