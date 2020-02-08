package com.hexbit.additionandsubtraction.data.model

/**
 * Equation model.
 *
 * Operation is mathematical representation of operation.
 * Correct answer is just answer of whole equation.
 *
 * For example: 5-2: componentA = 5, componentB = 2, operation = MINUS, correctAnswer = 3
 *
 *
 * Model równania zadania.
 * Operation to matematyczna reprezentacja działania.
 */
data class Equation(
    val componentA: Int,
    val componentB: Int,
    val operation: Operation,
    val correctAnswer: Int
)