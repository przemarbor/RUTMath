package com.hexbit.rutmath.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexbit.rutmath.shared.game.EquationGenerator
import com.hexbit.rutmath.shared.model.Equation
import com.hexbit.rutmath.shared.model.Operation
import com.hexbit.rutmath.shared.model.Score
import com.hexbit.rutmath.shared.usecase.DataUseCase
import kotlinx.coroutines.launch

/**
 * ViewModel for battle mode games using shared KMP logic.
 * This replaces the old BattleFragmentViewModel with shared business logic.
 */
class SharedBattleViewModel(
    private val equationGenerator: EquationGenerator,
    private val dataUseCase: DataUseCase
) : ViewModel() {
    
    private val _currentEquation = MutableLiveData<Equation>()
    val currentEquation: LiveData<Equation> = _currentEquation
    
    private val _answerChoices = MutableLiveData<List<Int>>()
    val answerChoices: LiveData<List<Int>> = _answerChoices
    
    private val _saveScoreFinished = MutableLiveData<Unit>()
    val saveScoreFinished: LiveData<Unit> = _saveScoreFinished
    
    private var maxNumber: Int = 100
    
    init {
        loadSettings()
    }
    
    /**
     * Load settings and start the first equation.
     */
    private fun loadSettings() {
        viewModelScope.launch {
            try {
                val settings = dataUseCase.getSettings()
                maxNumber = settings.maxNumberInBattleMode
                loadNextEquation()
            } catch (e: Exception) {
                // Use default settings on error
                maxNumber = 100
                loadNextEquation()
            }
        }
    }
    
    /**
     * Generates and loads the next equation for battle mode.
     */
    fun loadNextEquation() {
        viewModelScope.launch {
            try {
                val equation = generateBattleEquation()
                _currentEquation.value = equation
                
                // Generate answer choices
                val choices = equationGenerator.generateAnswers(equation.correctAnswer, 4)
                _answerChoices.value = choices
            } catch (e: Exception) {
                // Handle error - could emit error state
                e.printStackTrace()
            }
        }
    }
    
    /**
     * Validates if the provided answer is correct.
     */
    fun isAnswerCorrect(answer: Int): Boolean {
        return _currentEquation.value?.correctAnswer == answer
    }
    
    /**
     * Saves battle mode scores.
     */
    fun saveScoreInDatabase(
        player1Nick: String,
        player1Score: Int,
        player2Nick: String,
        player2Score: Int
    ) {
        viewModelScope.launch {
            try {
                val scoresToSave = mutableListOf<Score>()
                
                when {
                    player1Score > player2Score -> {
                        // Player 1 wins
                        scoresToSave.add(Score(player1Nick, player1Score))
                    }
                    player1Score < player2Score -> {
                        // Player 2 wins
                        scoresToSave.add(Score(player2Nick, player2Score))
                    }
                    else -> {
                        // Draw - save both scores
                        scoresToSave.add(Score(player1Nick, player1Score))
                        scoresToSave.add(Score(player2Nick, player2Score))
                    }
                }
                
                dataUseCase.saveGameScores(scoresToSave)
                _saveScoreFinished.value = Unit
            } catch (e: Exception) {
                // Handle error - could emit error state
                e.printStackTrace()
            }
        }
    }
    
    /**
     * Generates a random equation for battle mode.
     * This follows the original logic from BattleFragmentViewModel.
     */
    private fun generateBattleEquation(): Equation {
        val operations = listOf(
            Operation.PLUS,
            Operation.MINUS,
            Operation.MULTIPLY,
            Operation.DIVIDE
        )
        
        val selectedOperation = operations.random()
        return equationGenerator.generateEquation(selectedOperation, maxNumber)
    }
} 