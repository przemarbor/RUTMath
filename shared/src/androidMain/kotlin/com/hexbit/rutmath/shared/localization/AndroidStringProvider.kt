package com.hexbit.rutmath.shared.localization

import android.content.Context

/**
 * Android implementation of StringProvider using Android string resources.
 * Uses resource names to avoid circular dependencies.
 */
class AndroidStringProvider(private val context: Context) : StringProvider {
    
    private fun getString(resourceName: String): String {
        return try {
            val resourceId = context.resources.getIdentifier(resourceName, "string", context.packageName)
            if (resourceId != 0) {
                context.getString(resourceId)
            } else {
                resourceName // fallback to resource name if not found
            }
        } catch (e: Exception) {
            resourceName // fallback to resource name on error
        }
    }
    
    private fun getString(resourceName: String, vararg formatArgs: Any): String {
        return try {
            val resourceId = context.resources.getIdentifier(resourceName, "string", context.packageName)
            if (resourceId != 0) {
                context.getString(resourceId, *formatArgs)
            } else {
                resourceName // fallback to resource name if not found
            }
        } catch (e: Exception) {
            resourceName // fallback to resource name on error
        }
    }
    
    override fun ok(): String = getString("ok")
    override fun cancel(): String = getString("cancel")
    override fun yes(): String = getString("yes")
    override fun no(): String = getString("no")
    override fun error(): String = getString("error")
    override fun save(): String = getString("save")
    
    override fun score(): String = getString("score")
    override fun player1(): String = getString("player1")
    override fun player2(): String = getString("player2")
    override fun scoreboard(): String = getString("scoreboard")
    
    override fun additionSubtraction(): String = getString("fragment_modes_add_sub")
    override fun multiplicationDivision(): String = getString("fragment_modes_mul_div")
    override fun divisibility(): String = getString("fragment_modes_divisibility")
    override fun unitConversion(): String = getString("fragment_modes_units")
    override fun multiplicationTable(): String = getString("fragment_modes_table")
    
    override fun difficultyEasy(): String = getString("divisibility_difficulty_1")
    override fun difficultyMedium(): String = getString("divisibility_difficulty_2")
    override fun difficultyHard(): String = getString("divisibility_difficulty_3")
    override fun difficultyVeryHard(): String = getString("divisibility_difficulty_4")
    
    override fun unitsTime(): String = getString("units_time")
    override fun unitsLength(): String = getString("units_length")
    override fun unitsWeight(): String = getString("units_weight")
    override fun unitsSurface(): String = getString("units_surface")
    override fun unitsAll(): String = getString("units_all")
    
    override fun divisibilityQuestionPart1(): String = getString("divisibility_question_p1")
    override fun divisibilityQuestionPart2(): String = getString("divisibility_question_p2")
    
    override fun gameEnded(): String = getString("battle_game_ended")
    override fun nicknameEmpty(): String = getString("nick_empty")
    override fun nicknameExists(): String = getString("choose_player_nick_exist")
    
    override fun duelScore(player1Name: String, player1Score: Int, player2Name: String, player2Score: Int): String {
        return getString("duel_score", player1Name, player1Score, player2Name, player2Score)
    }
    
    override fun scoreboardFormat(position: Int, playerName: String): String {
        return getString("scoreboard_format", position, playerName)
    }
    
    override fun battleBonus(bonus: Int): String {
        return getString("battle_view_bonus", bonus)
    }
} 