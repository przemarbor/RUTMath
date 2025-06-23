package com.hexbit.rutmath.data.model

/**
 * Equation model.
 *
 * @param componentA value A
 * @param componentB value B
 * @param operation mathematical representation of operation.
 * @param correctAnswer answer of whole equation.
 *
 * For example: (5+2) <=> (componentA = 5, componentB = 2, operation = PLUS, correctAnswer = 7)
 *
 */

data class Equation(
    val componentA: Int,
    val componentB: Int,
    val operation: Operation,
    val correctAnswer: Int
)
