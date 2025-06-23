package com.hexbit.rutmath.data.model

/**
 * EquationUnits model.
 *
 * @param componentA value to convert
 * @param componentAUnitId ID of the unit of the componentA value
 * @param operation mathematical representation of units exchange operation.
 * @param correctAnswer result of exchange.
 * @param answerUnitId ID of the unit of the correctAnswer value
 *
 * For example:
 *    (5 meters = 50 decimeters)
 *    <=>
 *    (componentA = 5,
 *    componentAUnitId = 1 (m),
 *    correctAnswer = 50,
 *    answerUnitId = 2 (dm),
 *    operation = Operation.UnitsLength)
 */

data class EquationUnits(
    val componentA: Int,
    val componentAUnitId: Int,
    val operation: Operation,
    val correctAnswer: Int,
    val answerUnitId: Int
)