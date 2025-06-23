package com.hexbit.rutmath.shared.localization

/**
 * Cross-platform string provider interface for localization.
 * Platform-specific implementations will provide localized strings.
 */
interface StringProvider {
    
    // Basic strings
    fun ok(): String
    fun cancel(): String
    fun yes(): String
    fun no(): String
    fun error(): String
    fun save(): String
    
    // Game strings
    fun score(): String
    fun player1(): String
    fun player2(): String
    fun scoreboard(): String
    
    // Game modes
    fun additionSubtraction(): String
    fun multiplicationDivision(): String
    fun divisibility(): String
    fun unitConversion(): String
    fun multiplicationTable(): String
    
    // Difficulties
    fun difficultyEasy(): String
    fun difficultyMedium(): String
    fun difficultyHard(): String
    fun difficultyVeryHard(): String
    
    // Units
    fun unitsTime(): String
    fun unitsLength(): String
    fun unitsWeight(): String
    fun unitsSurface(): String
    fun unitsAll(): String
    
    // Divisibility
    fun divisibilityQuestionPart1(): String // "Is"
    fun divisibilityQuestionPart2(): String // "divisible by"
    
    // Game feedback
    fun gameEnded(): String
    fun nicknameEmpty(): String
    fun nicknameExists(): String
    
    // Formatted strings
    fun duelScore(player1Name: String, player1Score: Int, player2Name: String, player2Score: Int): String
    fun scoreboardFormat(position: Int, playerName: String): String
    fun battleBonus(bonus: Int): String
} 