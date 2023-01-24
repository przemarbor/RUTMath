package com.hexbit.rutmath.data.model

/**
 * EquationUnits model.
 *
 * Operation is mathematical representation of units exchange.
 * Correct answer is just answer of whole equation.
 *
 * For example: 5m = 50dm
 *              componentA = 5, componentAUnitId = 1 (m), correctAnswer = 50, answerUnitId = 2 (dm), operation = Operation.UnitsLength
 *
 */

data class EquationUnits(
    val componentA: Int,
    val componentAUnitId: Int,
    val operation: Operation,
    val correctAnswer: Int,
    val answerUnitId: Int

)