package com.hexbit.rutmath.shared.model

import kotlinx.serialization.Serializable

/**
 * Settings model for application configuration.
 *
 * @param maxNumberInBattleMode maximum number that can appear in battle mode equations
 * @param lastNickname1 last used nickname for player 1
 * @param lastNickname2 last used nickname for player 2
 * @param language application language code (e.g., "en", "pl", "fr", "pt")
 */
@Serializable
data class Settings(
    val maxNumberInBattleMode: Int = 100,
    val lastNickname1: String = "Player 1",
    val lastNickname2: String = "Player 2",
    val language: String = "en"
) 