package com.hexbit.rutmath.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexbit.rutmath.shared.game.AnswerResult
import com.hexbit.rutmath.shared.game.GameSession
import com.hexbit.rutmath.shared.game.GameState
import com.hexbit.rutmath.shared.model.Equation
import com.hexbit.rutmath.shared.model.ExerciseType
import com.hexbit.rutmath.shared.usecase.GameUseCase
import com.hexbit.rutmath.shared.usecase.DataUseCase
import kotlinx.coroutines.launch

/**
 * ViewModel for normal mathematical games using shared KMP logic.
 * This replaces the old NormalGameViewModel with shared business logic.
 */
class SharedNormalGameViewModel(
    private val gameUseCase: GameUseCase,
    private val dataUseCase: DataUseCase
) : ViewModel() {
    
    private val _gameState = MutableLiveData<GameState>()
    val gameState: LiveData<GameState> = _gameState
    
    private val _currentEquation = MutableLiveData<Equation>()
    val currentEquation: LiveData<Equation> = _currentEquation
    
    private val _answerChoices = MutableLiveData<List<Int>>()
    val answerChoices: LiveData<List<Int>> = _answerChoices
    
    private val _gameProgress = MutableLiveData<String>()
    val gameProgress: LiveData<String> = _gameProgress
    
    private val _endGameEvent = MutableLiveData<Int>()
    val endGameEvent: LiveData<Int> = _endGameEvent
    
    private val _answerValidation = MutableLiveData<AnswerValidation>()
    val answerValidation: LiveData<AnswerValidation> = _answerValidation
    
    private var gameSession: GameSession? = null
    private var exerciseType: ExerciseType? = null
    
    sealed class AnswerValidation {
        object Correct : AnswerValidation()
        object Incorrect : AnswerValidation()
    }
    
    /**
     * Starts a new game with the given exercise type.
     */
    fun startGame(exerciseType: ExerciseType) {
        this.exerciseType = exerciseType
        _gameState.value = GameState.Loading
        
        viewModelScope.launch {
            try {
                gameSession = gameUseCase.startGame(exerciseType)
                gameSession?.let { session ->
                    val firstEquation = session.getCurrentEquation()
                    _currentEquation.value = firstEquation
                    
                    // Generate answer choices for multiple choice
                    val choices = gameUseCase.generateAnswerChoices(firstEquation.correctAnswer)
                    _answerChoices.value = choices
                    
                    updateProgress()
                    _gameState.value = GameState.Playing
                }
            } catch (e: Exception) {
                _gameState.value = GameState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    /**
     * Submits user's answer and handles the result.
     */
    fun submitAnswer(answer: Int) {
        viewModelScope.launch {
            gameSession?.let { session ->
                try {
                    val isCorrect = gameUseCase.validateAnswer(session.getCurrentEquation(), answer)
                    _answerValidation.value = if (isCorrect) AnswerValidation.Correct else AnswerValidation.Incorrect
                    
                    when (val result = gameUseCase.submitAnswer(session, answer)) {
                        is AnswerResult.Continue -> {
                            result.nextEquation?.let { nextEquation ->
                                _currentEquation.value = nextEquation
                                
                                // Generate new answer choices
                                val choices = gameUseCase.generateAnswerChoices(nextEquation.correctAnswer)
                                _answerChoices.value = choices
                                
                                updateProgress()
                            }
                        }
                        is AnswerResult.GameEnd -> {
                            _gameState.value = GameState.Finished(result.finalRate)
                            _endGameEvent.value = result.finalRate
                            
                            // Update exercise progress
                            exerciseType?.let { exercise ->
                                val updatedExercise = dataUseCase.updateExerciseProgress(exercise, result.finalRate)
                                dataUseCase.checkAndUnlockNextExercise(updatedExercise, result.finalRate)
                            }
                        }
                        else -> {
                            _gameState.value = GameState.Error("Unexpected game result")
                        }
                    }
                } catch (e: Exception) {
                    _gameState.value = GameState.Error(e.message ?: "Unknown error")
                }
            }
        }
    }
    
    /**
     * Provides direct input validation for text-based answers.
     */
    fun validateDirectAnswer(answer: Int) {
        viewModelScope.launch {
            gameSession?.let { session ->
                val isCorrect = gameUseCase.validateAnswer(session.getCurrentEquation(), answer)
                _answerValidation.value = if (isCorrect) AnswerValidation.Correct else AnswerValidation.Incorrect
            }
        }
    }
    
    /**
     * Proceeds to next question after answer validation.
     */
    fun proceedToNextQuestion() {
        viewModelScope.launch {
            gameSession?.let { session ->
                try {
                    when (val result = gameUseCase.submitAnswer(session, session.getCurrentEquation().correctAnswer)) {
                        is AnswerResult.Continue -> {
                            result.nextEquation?.let { nextEquation ->
                                _currentEquation.value = nextEquation
                                
                                // Generate new answer choices
                                val choices = gameUseCase.generateAnswerChoices(nextEquation.correctAnswer)
                                _answerChoices.value = choices
                                
                                updateProgress()
                            }
                        }
                        is AnswerResult.GameEnd -> {
                            _gameState.value = GameState.Finished(result.finalRate)
                            _endGameEvent.value = result.finalRate
                            
                            // Update exercise progress
                            exerciseType?.let { exercise ->
                                val updatedExercise = dataUseCase.updateExerciseProgress(exercise, result.finalRate)
                                dataUseCase.checkAndUnlockNextExercise(updatedExercise, result.finalRate)
                            }
                        }
                        else -> {
                            _gameState.value = GameState.Error("Unexpected game result")
                        }
                    }
                } catch (e: Exception) {
                    _gameState.value = GameState.Error(e.message ?: "Unknown error")
                }
            }
        }
    }
    
    /**
     * Gets current game progress information.
     */
    private suspend fun updateProgress() {
        gameSession?.let { session ->
            val progress = gameUseCase.getGameProgress(session)
            val progressText = "${progress.currentQuestionIndex + 1}/${progress.totalQuestions}"
            _gameProgress.value = progressText
        }
    }
    
    /**
     * Resets the game state.
     */
    fun resetGame() {
        gameSession = null
        exerciseType = null
        _gameState.value = GameState.Idle
    }
} 