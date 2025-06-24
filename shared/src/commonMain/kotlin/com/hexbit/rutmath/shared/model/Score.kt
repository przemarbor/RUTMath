package com.hexbit.rutmath.shared.model

import kotlinx.serialization.Serializable

/**
 * Score model representing a player's score in a game.
 *
 * @param playerName name of the player who achieved this score
 * @param score the score value achieved
 * @param id unique identifier for the score record
 */
@Serializable
data class Score(
    val playerName: String,
    val score: Int,
    val id: Int = 0
) 