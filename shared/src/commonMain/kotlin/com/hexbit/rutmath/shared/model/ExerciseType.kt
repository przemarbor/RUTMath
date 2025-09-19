package com.octbit.rutmath.shared.model

import kotlinx.serialization.Serializable

/**
 * ExerciseType is a data model representing a type of mathematical exercise.
 * 
 * @param operation type of mathematical operation
 * @param difficulty modifies difficulty of the Exercise. For example: when difficulty equals to 10
 *      and operation equals to Operation.PLUS then user will see only exercises with answer
 *      that is equal or less than 10.
 *      Every operation has specific difficulty range.
 *      For example:
 *          Operation.PLUS can use difficulty range: (5-200).
 *          Operation.DIVISIBILITY can use difficulty range: (1-10).
 *      Check specific GameViewModel for specific difficulty range.
 * @param rate number of stars reached in this type of exercises.
 * @param userNick nickname of the user who played this exercise type
 * @param isUnlocked whether exercise is unlocked and can be accessed by the user.
 * @param id unique identification number.
 */
@Serializable
data class ExerciseType(
    val operation: Operation,
    val difficulty: Int,
    val rate: Int = 0,
    val userNick: String,
    val isUnlocked: Boolean,
    val id: Int = 0
) 