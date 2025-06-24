package com.hexbit.rutmath.shared.usecase

import com.hexbit.rutmath.shared.game.AnswerResult
import com.hexbit.rutmath.shared.game.EquationGenerator
import com.hexbit.rutmath.shared.game.MathEquationGenerator
import com.hexbit.rutmath.shared.game.UnitsGameSession
import com.hexbit.rutmath.shared.model.EquationUnits
import com.hexbit.rutmath.shared.model.ExerciseType
import com.hexbit.rutmath.shared.model.Operation

/**
 * Use case for managing unit conversion games.
 */
class UnitsGameUseCase(
    private val equationGenerator: EquationGenerator
) {
    companion object {
        const val UNITS_EXERCISES_COUNT = 20
    }
    
    /**
     * Starts a new units game session.
     */
    suspend fun startGame(exerciseType: ExerciseType): UnitsGameSession {
        val equations = equationGenerator.generateUnitsEquations(exerciseType, UNITS_EXERCISES_COUNT)
        
        if (equations.isEmpty()) {
            throw IllegalStateException("No units equations could be generated for the given exercise type")
        }
        
        return UnitsGameSession(equations, exerciseType)
    }
    
    /**
     * Submits an answer for the current units equation.
     */
    suspend fun submitAnswer(gameSession: UnitsGameSession, answer: Int): AnswerResult {
        val currentEquation = gameSession.getCurrentEquation()
        val isCorrect = validateAnswer(currentEquation, answer)
        
        gameSession.recordAnswer(isCorrect)
        
        return if (gameSession.hasMoreEquations()) {
            val nextEquation = gameSession.nextEquation()
            AnswerResult.ContinueUnits(nextEquation)
        } else {
            val finalRate = gameSession.calculateGameRate()
            AnswerResult.GameEnd(finalRate)
        }
    }
    
    /**
     * Validates if the provided answer is correct.
     */
    suspend fun validateAnswer(equation: EquationUnits, answer: Int): Boolean {
        return equation.correctAnswer == answer
    }
    
    /**
     * Creates help text for unit conversions.
     */
    suspend fun createHelpText(operation: Operation): String {
        var result = ""
        var counter = 0
        val units: List<String>
        val ratio: List<Int>
        
        when (operation) {
            Operation.UNITS_TIME -> {
                units = MathEquationGenerator.UNITS_TIME
                ratio = MathEquationGenerator.RATIO_TIME
            }
            Operation.UNITS_LENGTH -> {
                units = MathEquationGenerator.UNITS_LENGTH
                ratio = MathEquationGenerator.RATIO_LENGTH
            }
            Operation.UNITS_WEIGHT -> {
                units = MathEquationGenerator.UNITS_WEIGHT
                ratio = MathEquationGenerator.RATIO_WEIGHT
            }
            Operation.UNITS_SURFACE -> {
                units = MathEquationGenerator.UNITS_SURFACE
                ratio = MathEquationGenerator.RATIO_SURFACE
            }
            else -> throw IllegalArgumentException("Invalid units operation: $operation")
        }
        
        while (counter < ratio.size) {
            result += "1${units[counter]} = ${ratio[counter]}${units[counter + 1]}\n"
            counter += 1
        }
        
        return result
    }
    
    /**
     * Gets the unit name for a given operation and unit ID.
     */
    suspend fun getUnitName(operation: Operation, unitId: Int): String {
        val units = when (operation) {
            Operation.UNITS_TIME -> MathEquationGenerator.UNITS_TIME
            Operation.UNITS_LENGTH -> MathEquationGenerator.UNITS_LENGTH
            Operation.UNITS_WEIGHT -> MathEquationGenerator.UNITS_WEIGHT
            Operation.UNITS_SURFACE -> MathEquationGenerator.UNITS_SURFACE
            else -> throw IllegalArgumentException("Invalid units operation: $operation")
        }
        
        return if (unitId in units.indices) units[unitId] else ""
    }
} 