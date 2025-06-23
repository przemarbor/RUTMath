package com.hexbit.rutmath.shared.game

import com.hexbit.rutmath.shared.model.Equation
import com.hexbit.rutmath.shared.model.EquationUnits
import com.hexbit.rutmath.shared.model.ExerciseType
import com.hexbit.rutmath.shared.model.Operation

/**
 * Interface for generating mathematical equations based on operation type and difficulty.
 */
interface EquationGenerator {
    /**
     * Generates a single equation based on operation and difficulty.
     *
     * @param operation The type of mathematical operation
     * @param difficulty The difficulty level (affects number ranges)
     * @return Generated equation
     */
    fun generateEquation(operation: Operation, difficulty: Int): Equation
    
    /**
     * Generates a units equation for unit conversion exercises.
     *
     * @param operation The type of units operation
     * @param difficulty The difficulty level
     * @return Generated units equation
     */
    fun generateUnitsEquation(operation: Operation, difficulty: Int): EquationUnits
    
    /**
     * Generates multiple answer choices including the correct answer.
     *
     * @param correctAnswer The correct answer to the equation
     * @param count Number of answer choices to generate (default 4)
     * @return List of answer choices (including correct answer)
     */
    fun generateAnswers(correctAnswer: Int, count: Int = 4): List<Int>
    
    /**
     * Generates a list of equations for a complete exercise.
     *
     * @param exerciseType The exercise type configuration
     * @param exerciseCount Number of equations to generate
     * @return List of generated equations
     */
    fun generateEquations(exerciseType: ExerciseType, exerciseCount: Int): List<Equation>
    
    /**
     * Generates a list of units equations for a complete exercise.
     *
     * @param exerciseType The exercise type configuration
     * @param exerciseCount Number of equations to generate
     * @return List of generated units equations
     */
    fun generateUnitsEquations(exerciseType: ExerciseType, exerciseCount: Int): List<EquationUnits>
} 