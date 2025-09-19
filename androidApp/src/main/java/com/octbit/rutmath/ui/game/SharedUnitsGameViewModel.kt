package com.octbit.rutmath.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octbit.rutmath.shared.game.AnswerResult
import com.octbit.rutmath.shared.game.UnitsGameSession
import com.octbit.rutmath.shared.model.EquationUnits
import com.octbit.rutmath.shared.model.ExerciseType
import com.octbit.rutmath.shared.usecase.UnitsGameUseCase
import com.octbit.rutmath.shared.usecase.DataUseCase
import kotlinx.coroutines.launch

/**
 * ViewModel for units conversion games using shared KMP logic.
 * This replaces the old UnitsGameViewModel with shared business logic.
 */
class SharedUnitsGameViewModel(
    private val unitsGameUseCase: UnitsGameUseCase,
    private val dataUseCase: DataUseCase
) : ViewModel() {
    
    private val _currentEquation = MutableLiveData<EquationUnits>()
    val currentEquation: LiveData<EquationUnits> = _currentEquation
    
    private val _gameProgress = MutableLiveData<String>()
    val gameProgress: LiveData<String> = _gameProgress
    
    private val _endGameEvent = MutableLiveData<Int>()
    val endGameEvent: LiveData<Int> = _endGameEvent
    
    private val _answerValidation = MutableLiveData<AnswerValidation>()
    val answerValidation: LiveData<AnswerValidation> = _answerValidation
    
    private val _helpText = MutableLiveData<String>()
    val helpText: LiveData<String> = _helpText
    
    private val _unitNames = MutableLiveData<Pair<String, String>>()
    val unitNames: LiveData<Pair<String, String>> = _unitNames
    
    private var gameSession: UnitsGameSession? = null
    private var exerciseType: ExerciseType? = null
    
    sealed class AnswerValidation {
        object Correct : AnswerValidation()
        object Incorrect : AnswerValidation()
    }
    
    /**
     * Starts a new units game with the given exercise type.
     */
    fun startGame(exerciseType: ExerciseType) {
        this.exerciseType = exerciseType
        
        viewModelScope.launch {
            try {
                gameSession = unitsGameUseCase.startGame(exerciseType)
                gameSession?.let { session ->
                    val firstEquation = session.getCurrentEquation()
                    _currentEquation.value = firstEquation
                    
                    updateUnitNames(firstEquation)
                    updateProgress()
                    updateHelpText()
                }
            } catch (e: Exception) {
                // Handle error
                e.printStackTrace()
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
                    val isCorrect = unitsGameUseCase.validateAnswer(session.getCurrentEquation(), answer)
                    _answerValidation.value = if (isCorrect) AnswerValidation.Correct else AnswerValidation.Incorrect
                    
                    when (val result = unitsGameUseCase.submitAnswer(session, answer)) {
                        is AnswerResult.ContinueUnits -> {
                            result.nextEquation?.let { nextEquation ->
                                _currentEquation.value = nextEquation
                                updateUnitNames(nextEquation)
                                updateProgress()
                            }
                        }
                        is AnswerResult.GameEnd -> {
                            _endGameEvent.value = result.finalRate
                            
                            // Update exercise progress
                            exerciseType?.let { exercise ->
                                val updatedExercise = dataUseCase.updateExerciseProgress(exercise, result.finalRate)
                                dataUseCase.checkAndUnlockNextExercise(updatedExercise, result.finalRate)
                            }
                        }
                        else -> {
                            // Handle unexpected result
                        }
                    }
                } catch (e: Exception) {
                    // Handle error
                    e.printStackTrace()
                }
            }
        }
    }
    
    /**
     * Validates answer without proceeding to next question.
     */
    fun validateAnswer(answer: Int) {
        viewModelScope.launch {
            gameSession?.let { session ->
                val isCorrect = unitsGameUseCase.validateAnswer(session.getCurrentEquation(), answer)
                _answerValidation.value = if (isCorrect) AnswerValidation.Correct else AnswerValidation.Incorrect
            }
        }
    }
    
    /**
     * Marks current equation as failed and proceeds.
     */
    fun markCurrentEquationAsFailed() {
        gameSession?.recordAnswer(false)
        proceedToNextQuestion()
    }
    
    /**
     * Proceeds to next question after manual validation.
     */
    fun proceedToNextQuestion() {
        viewModelScope.launch {
            gameSession?.let { session ->
                try {
                    if (session.hasMoreEquations()) {
                        val nextEquation = session.nextEquation()
                        nextEquation?.let {
                            _currentEquation.value = it
                            updateUnitNames(it)
                            updateProgress()
                        }
                    } else {
                        val finalRate = session.calculateGameRate()
                        _endGameEvent.value = finalRate
                        
                        // Update exercise progress
                        exerciseType?.let { exercise ->
                            val updatedExercise = dataUseCase.updateExerciseProgress(exercise, finalRate)
                            dataUseCase.checkAndUnlockNextExercise(updatedExercise, finalRate)
                        }
                    }
                } catch (e: Exception) {
                    // Handle error
                    e.printStackTrace()
                }
            }
        }
    }
    
    /**
     * Updates the help text for current operation.
     */
    private fun updateHelpText() {
        viewModelScope.launch {
            exerciseType?.let { exercise ->
                try {
                    val helpText = unitsGameUseCase.createHelpText(exercise.operation)
                    _helpText.value = helpText
                } catch (e: Exception) {
                    _helpText.value = ""
                }
            }
        }
    }
    
    /**
     * Updates unit names for the current equation.
     */
    private fun updateUnitNames(equation: EquationUnits) {
        viewModelScope.launch {
            try {
                val sourceUnit = unitsGameUseCase.getUnitName(equation.operation, equation.componentAUnitId)
                val targetUnit = unitsGameUseCase.getUnitName(equation.operation, equation.answerUnitId)
                _unitNames.value = Pair(sourceUnit, targetUnit)
            } catch (e: Exception) {
                _unitNames.value = Pair("", "")
            }
        }
    }
    
    /**
     * Updates game progress information.
     */
    private fun updateProgress() {
        gameSession?.let { session ->
            val progressText = "${session.getCurrentEquationIndex() + 1}/${session.getTotalEquationsCount()}"
            _gameProgress.value = progressText
        }
    }
} 
