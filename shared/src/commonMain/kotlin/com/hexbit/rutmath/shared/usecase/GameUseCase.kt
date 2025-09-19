package com.octbit.rutmath.shared.usecase

import com.octbit.rutmath.shared.game.AnswerResult
import com.octbit.rutmath.shared.game.EquationGenerator
import com.octbit.rutmath.shared.game.GameSession
import com.octbit.rutmath.shared.model.Equation
import com.octbit.rutmath.shared.model.ExerciseType

/**
 * Use case for managing regular mathematical games.
 */
class GameUseCase(
    private val equationGenerator: EquationGenerator
) {
    companion object {
        const val DEFAULT_EXERCISES_COUNT = 20
        const val TABLE_EXERCISES_COUNT = 20
        const val DIVISIBILITY_EXERCISES_COUNT = 30
    }
    
    /**
     * Starts a new game session.
     *
     * @param exerciseType The type of exercise to play
     * @return GameSession representing the started game
     */
    suspend fun startGame(exerciseType: ExerciseType): GameSession {
        val exerciseCount = when (exerciseType.operation) {
            com.octbit.rutmath.shared.model.Operation.MULTIPLY -> TABLE_EXERCISES_COUNT
            com.octbit.rutmath.shared.model.Operation.DIVISIBILITY -> DIVISIBILITY_EXERCISES_COUNT
            else -> DEFAULT_EXERCISES_COUNT
        }
        
        val equations = equationGenerator.generateEquations(exerciseType, exerciseCount)
        
        if (equations.isEmpty()) {
            throw IllegalStateException("No equations could be generated for the given exercise type")
        }
        
        return GameSession(equations, exerciseType)
    }
    
    /**
     * Submits an answer for the current equation.
     *
     * @param gameSession The current game session
     * @param answer The user's answer
     * @return AnswerResult indicating the result and next action
     */
    suspend fun submitAnswer(gameSession: GameSession, answer: Int): AnswerResult {
        val currentEquation = gameSession.getCurrentEquation()
        val isCorrect = validateAnswer(currentEquation, answer)
        
        gameSession.recordAnswer(isCorrect)
        
        return if (gameSession.hasMoreEquations()) {
            val nextEquation = gameSession.nextEquation()
            AnswerResult.Continue(nextEquation)
        } else {
            val finalRate = gameSession.calculateGameRate()
            AnswerResult.GameEnd(finalRate)
        }
    }
    
    /**
     * Validates if the provided answer is correct for the given equation.
     *
     * @param equation The equation to validate against
     * @param answer The user's answer
     * @return true if the answer is correct, false otherwise
     */
    suspend fun validateAnswer(equation: Equation, answer: Int): Boolean {
        return equation.correctAnswer == answer
    }
    
    /**
     * Generates answer choices for multiple choice questions.
     *
     * @param correctAnswer The correct answer
     * @param choicesCount Number of choices to generate
     * @return List of answer choices (shuffled)
     */
    suspend fun generateAnswerChoices(correctAnswer: Int, choicesCount: Int = 4): List<Int> {
        return equationGenerator.generateAnswers(correctAnswer, choicesCount)
    }
    
    /**
     * Gets the current progress of the game.
     *
     * @param gameSession The current game session
     * @return GameProgress with current statistics
     */
    suspend fun getGameProgress(gameSession: GameSession): GameProgress {
        return GameProgress(
            currentQuestionIndex = gameSession.getCurrentEquationIndex(),
            totalQuestions = gameSession.getTotalEquationsCount(),
            correctAnswers = gameSession.getCorrectAnswersCount(),
            incorrectAnswers = gameSession.getIncorrectAnswersCount()
        )
    }
}

/**
 * Data class representing game progress.
 */
data class GameProgress(
    val currentQuestionIndex: Int,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val incorrectAnswers: Int
) {
    val completionPercentage: Float
        get() = if (totalQuestions > 0) (currentQuestionIndex.toFloat() / totalQuestions.toFloat()) * 100f else 0f
    
    val accuracy: Float
        get() = if (correctAnswers + incorrectAnswers > 0) {
            (correctAnswers.toFloat() / (correctAnswers + incorrectAnswers).toFloat()) * 100f
        } else 0f
} 