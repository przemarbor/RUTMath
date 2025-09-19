package com.octbit.rutmath.shared.model

import kotlinx.serialization.Serializable

/**
 * Player model representing a game player.
 *
 * @param nickname player's nickname
 * @param id unique identifier for the player
 */
@Serializable
data class Player(
    val nickname: String,
    val id: Int = 0
) 