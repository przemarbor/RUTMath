package com.octbit.rutmath.shared.model

import kotlinx.serialization.Serializable

/**
 * Types of operations that can exists in exercises.
 */
@Serializable
enum class Operation {
    PLUS,
    MINUS,
    PLUS_MINUS,
    MULTIPLY,
    DIVIDE,
    MULTIPLY_DIVIDE,
    DIVISIBILITY,
    UNITS_TIME,
    UNITS_LENGTH,
    UNITS_WEIGHT,
    UNITS_SURFACE,
    UNITS_ALL,
    // Adding negative plus and negative minus
    NEGATIVE_PLUS,
    NEGATIVE_MINUS,
    NEGATIVE_PLUS_MINUS,
    NEGATIVE_MUL,
    NEGATIVE_DIV,
    NEGATIVE_MUL_DIV
} 