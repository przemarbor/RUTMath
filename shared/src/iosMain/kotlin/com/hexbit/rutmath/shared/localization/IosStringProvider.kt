package com.hexbit.rutmath.shared.localization

import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.NSLocalizedString

/**
 * iOS implementation of StringProvider using NSLocalizedString.
 * For now uses hardcoded English strings, but can be extended to use iOS localization.
 */
class IosStringProvider : StringProvider {
    
    override fun ok(): String = NSLocalizedString("OK", "")
    override fun cancel(): String = NSLocalizedString("Cancel", "")
    override fun yes(): String = NSLocalizedString("Yes", "")
    override fun no(): String = NSLocalizedString("No", "")
    override fun error(): String = NSLocalizedString("Error", "")
    override fun save(): String = NSLocalizedString("Save", "")
    
    override fun score(): String = NSLocalizedString("Score:", "")
    override fun player1(): String = NSLocalizedString("Player1", "")
    override fun player2(): String = NSLocalizedString("Player2", "")
    override fun scoreboard(): String = NSLocalizedString("Scoreboard:", "")
    
    override fun additionSubtraction(): String = NSLocalizedString("Addition and Subtraction", "")
    override fun multiplicationDivision(): String = NSLocalizedString("Multiplication and Division", "")
    override fun divisibility(): String = NSLocalizedString("Divisibility", "")
    override fun unitConversion(): String = NSLocalizedString("Unit conversion", "")
    override fun multiplicationTable(): String = NSLocalizedString("Multiplication table", "")
    
    override fun difficultyEasy(): String = NSLocalizedString("Easy", "")
    override fun difficultyMedium(): String = NSLocalizedString("Medium", "")
    override fun difficultyHard(): String = NSLocalizedString("Hard", "")
    override fun difficultyVeryHard(): String = NSLocalizedString("Very Hard", "")
    
    override fun unitsTime(): String = NSLocalizedString("Time", "")
    override fun unitsLength(): String = NSLocalizedString("Length", "")
    override fun unitsWeight(): String = NSLocalizedString("Weight", "")
    override fun unitsSurface(): String = NSLocalizedString("Surface", "")
    override fun unitsAll(): String = NSLocalizedString("All", "")
    
    override fun divisibilityQuestionPart1(): String = NSLocalizedString("Is", "")
    override fun divisibilityQuestionPart2(): String = NSLocalizedString("divisible by", "")
    
    override fun gameEnded(): String = NSLocalizedString("Game ended", "")
    override fun nicknameEmpty(): String = NSLocalizedString("Nickname can not be empty!", "")
    override fun nicknameExists(): String = NSLocalizedString("Nickname exists!", "")
    
    override fun duelScore(player1Name: String, player1Score: Int, player2Name: String, player2Score: Int): String {
        return "$player1Name score: $player1Score\n$player2Name score: $player2Score"
    }
    
    override fun scoreboardFormat(position: Int, playerName: String): String {
        return "$position. $playerName"
    }
    
    override fun battleBonus(bonus: Int): String {
        return "Sequential bonus +$bonus"
    }
} 