package com.hexbit.additionandsubtraction.data.model

import androidx.room.TypeConverter

/**
 * Types of operations that can exists in exercises.
 *
 * Rodzaje operacji jakie mogą wystąpić w zadaniach
 */
enum class Operation {
    PLUS,
    MINUS,
    PLUS_MINUS
}

/**
 * Converter for database. It is not primitive type then it will be stored in database as String.
 * When database will retrieve it will be converted from String to Operation with stringToOperation method.
 *
 * Konweter obiektu Operation do i z bazy danych. W bazie przechowywany jest jako String.
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