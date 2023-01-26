package com.hexbit.rutmath.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * ExerciseType is a database model.
 * @param difficulty modifies difficulty of the Exercise. For example: when difficulty equals to 10
 *      and operation equals to Operation.PLUS then user will see only exercises with answer
 *      that is equal or less than 10.
 *      Every operation has specific difficulty range.
 *      For example:
 *          Operation.PLUS can use difficulty range: (5-200).
 *          Operation.DIVISIBILITY can use difficulty range: (1-10).
 *      Check specific GameViewModel for specific difficulty range.
 * @param rate number of stars reached in this type of exercises.
 * @param unlocked whether exercise is unlocked and can be accessed by the user.
 * @param id unique identification number in database.
 */

@Entity(tableName = "ExerciseType")
data class ExerciseType(
    @ColumnInfo(name = "operation")
    var operation: Operation,
    @ColumnInfo(name = "difficulty")
    var difficulty: Int,
    @ColumnInfo(name = "rate")
    var rate: Int = 0,
    val userNick: String,
    var unlocked: Boolean,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
) : Serializable
