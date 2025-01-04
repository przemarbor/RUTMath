package com.hexbit.rutmath.data.model

import androidx.room.TypeConverter

/**
 * Types of operations that can exists in exercises.
 */

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
    //adding negative plus and negative minus
    NEGATIVE_PLUS,
    NEGATIVE_MINUS,
    NEGATIVE_PLUS_MINUS,
    //new code added here
    NEGATIVE_PLUS_MUL,
    NEGATIVE_MINUS_MUL,
    NEGATIVE_PLUS_DIV,
    NEGATIVE_MINUS_DIV,
    NEGATIVE_PLUS_MINUS_MUL,
    NEGATIVE_PLUS_MINUS_DIV
}

/**
 * Converter for database. It is not primitive type then it will be stored in database as String.
 * When database will retrieve it will be converted from String to Operation with stringToOperation method.
 */
class OperationConverter {
    @TypeConverter
    fun stringToOperation(value: String): Operation {
        return Operation.valueOf(value)
    }

    @TypeConverter
    fun operationToString(value: Operation): String {
        return value.name
    }
}