package com.hexbit.rutmath.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Exercise type. It is also database model.
 * It contains operation (like PLUS/MINUS/PLUS_MINUS] type.
 * Max number that can exists in equations of that exercise. For example: when maxNumber equals to 10
 * then user will see only exercises with answer that is equals or less than 10.
 * Rate - number of stars reached in this type of exercises.
 * Id - unique identification number in database.
 *
 * //# Typ zadania z adnotacjami pozwalającymi na zapis w lokalnej bazie danych.
 */
@Entity(tableName = "ExerciseType")
data class ExerciseType(
    @ColumnInfo(name = "operation")
    var operation: Operation,
    @ColumnInfo(name = "maxNumber")
    var maxNumber: Int,
    @ColumnInfo(name = "rate")
    var rate: Int = 0,
    val userNick: String,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
) : Serializable
