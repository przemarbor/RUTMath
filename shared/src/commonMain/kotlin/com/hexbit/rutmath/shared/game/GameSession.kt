package com.hexbit.rutmath.shared.game

import com.hexbit.rutmath.shared.model.Equation
import com.hexbit.rutmath.shared.model.EquationUnits
import com.hexbit.rutmath.shared.model.ExerciseType
import kotlin.math.round

/**
 * Represents a game session with its current state and progress.
 */
class GameSession(
    private val equations: List<Equation>,
    private val exerciseType: ExerciseType
) {
    private var currentIndex = 0
    private val results = mutableListOf<Pair<Equation, Boolean>>()
    
    fun getCurrentEquation(): Equation = equations[currentIndex]
    
    fun recordAnswer(isCorrect: Boolean) {
        results.add(equations[currentIndex] to isCorrect)
    }
    
    fun nextEquation(): Equation? {
        currentIndex++
        return if (currentIndex < equations.size) equations[currentIndex] else null
    }
    
    fun hasMoreEquations(): Boolean = currentIndex < equations.size - 1
    
    fun getAllResults(): List<Pair<Equation, Boolean>> = results.toList()
    
    fun getCurrentEquationIndex(): Int = currentIndex
    
    fun getTotalEquationsCount(): Int = equations.size
    
    fun getCorrectAnswersCount(): Int = results.count { it.second }
    
    fun getIncorrectAnswersCount(): Int = results.count { !it.second }
    
    /**
     * Calculates game rate based on correct answers percentage.
     * Returns a value between 0-5 for most games, 0-100 for table games.
     */
    fun calculateGameRate(): Int {
        val correctAnswers = results.count { it.second }
        val percent = (correctAnswers.toFloat() / results.size.toFloat()) * 100
        
        return if (exerciseType.operation.name.contains("TABLE")) {
            // Table games use 0-100 scale
            round(percent).toInt()
        } else {
            // Other games use 0-5 scale
            round(percent / 20).toInt()
        }
    }
}

/**
 * Represents a units-based game session.
 */
class UnitsGameSession(
    private val equations: List<EquationUnits>,
    private val exerciseType: ExerciseType
) {
    private var currentIndex = 0
    private val results = mutableListOf<Pair<EquationUnits, Boolean>>()
    
    fun getCurrentEquation(): EquationUnits = equations[currentIndex]
    
    fun recordAnswer(isCorrect: Boolean) {
        results.add(equations[currentIndex] to isCorrect)
    }
    
    fun nextEquation(): EquationUnits? {
        currentIndex++
        return if (currentIndex < equations.size) equations[currentIndex] else null
    }
    
    fun hasMoreEquations(): Boolean = currentIndex < equations.size - 1
    
    fun getAllResults(): List<Pair<EquationUnits, Boolean>> = results.toList()
    
    fun getCurrentEquationIndex(): Int = currentIndex
    
    fun getTotalEquationsCount(): Int = equations.size
    
    fun getCorrectAnswersCount(): Int = results.count { it.second }
    
    fun getIncorrectAnswersCount(): Int = results.count { !it.second }
    
    fun calculateGameRate(): Int {
        val correctAnswers = results.count { it.second }
        val percent = (correctAnswers.toFloat() / results.size.toFloat()) * 100
        return round(percent / 20).toInt()
    }
}

/**
 * Sealed class representing different answer results.
 */
sealed class AnswerResult {
    data class Continue(val nextEquation: Equation?) : AnswerResult()
    data class ContinueUnits(val nextEquation: EquationUnits?) : AnswerResult()
    data class GameEnd(val finalRate: Int) : AnswerResult()
}

/**
 * Represents the current state of a game.
 */
sealed class GameState {
    object Idle : GameState()
    object Loading : GameState()
    object Playing : GameState()
    data class Finished(val rate: Int) : GameState()
    data class Error(val message: String) : GameState()
} 