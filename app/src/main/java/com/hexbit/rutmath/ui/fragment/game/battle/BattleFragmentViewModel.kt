package com.hexbit.rutmath.ui.fragment.game.battle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hexbit.rutmath.data.AppDatabase
import com.hexbit.rutmath.data.model.Equation
import com.hexbit.rutmath.data.model.Operation
import com.hexbit.rutmath.data.model.Score
import com.hexbit.rutmath.util.base.DisposableViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.random.nextInt

class BattleFragmentViewModel(private val database: AppDatabase) : DisposableViewModel() {

    private val answersDrawedEvent = MutableLiveData<IntArray>()

    private val onNextEquationActiveEvent = MutableLiveData<Equation>()

    private val saveScoreFinishedEvent = MutableLiveData<Unit>()

    private var maxNumber: Int = 0

    private lateinit var activeEquation: Equation

    init {
        manageDisposable {
            database.settingsDao().getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { settings ->
                    maxNumber = settings.first().maxNumberInBattleMode
                    activeEquation = drawEquation()
                    onNextEquationActiveEvent.value = activeEquation
                }
        }
    }

    fun getAnswersDrawedEvent(): LiveData<IntArray> = answersDrawedEvent

    fun getOnNextEquationActiveEvent(): LiveData<Equation> = onNextEquationActiveEvent

    fun getSaveScoreFinishedEvent(): LiveData<Unit> = saveScoreFinishedEvent

    fun loadNextEquation() {
        activeEquation = drawEquation()
        onNextEquationActiveEvent.value = activeEquation
    }

    
     // Draw equations for game and returns it.
     
     //# Losuje równanie dla gry i je zwraca.
     //# Zapewnia niepowtarzalność zadań z przeszłości.
     
    private fun drawEquation(): Equation {
        var a = 0
        var b = 0
        var correctAnswer = 0
        var operation = Operation.PLUS_MINUS
        val possibleValues = mutableListOf<Int>()
        when (Random.nextInt(1, 5)) {
            1 -> {
                a = Random.nextInt(1, maxNumber + 1)
                for (num in 1..maxNumber + 1){
                    if (a + num < maxNumber+1)
                        possibleValues.add(num)
                }
                if (possibleValues.any())
                    b = possibleValues.random()
                correctAnswer = a + b
                operation = Operation.PLUS
            }
            2 -> {
                a = Random.nextInt(1, maxNumber + 1)
                for (num in 1..maxNumber + 1){
                    if (a - num > 0)
                        possibleValues.add(num)
                }
                if (possibleValues.any())
                    b = possibleValues.random()
                correctAnswer = a - b
                operation = Operation.MINUS
            }
            3 -> {
                a = Random.nextInt(1, sqrt((maxNumber).toDouble()).roundToInt()+1)
                for (num in 1..maxNumber + 1){
                    if (a * num <= maxNumber+1)
                        possibleValues.add(num)
                }
                if (possibleValues.any())
                    b = possibleValues.random()
                correctAnswer = a * b
                operation = Operation.MULTIPLY
            }
            4 -> {
                b = Random.nextInt(1, sqrt((maxNumber).toDouble()).roundToInt()+1)
                for (num in 1..maxNumber+1){
                    if (num % b == 0)
                        possibleValues.add(num)
                }
                if (possibleValues.any())
                    a = possibleValues.random()
                correctAnswer = a / b
                operation = Operation.DIVIDE
            }
        }

        val equationToAdd = Equation(
            a,
            b,
            operation,
            correctAnswer
        )

        drawAnswers(equationToAdd.correctAnswer)
        return equationToAdd
    }


    private fun drawAnswers(correctAnswer: Int) {
        val result = arrayListOf<Int>()
        result.add(correctAnswer)
        while (result.size < 4) {
            val number =
                Random.nextInt(
                    IntRange(
                        1,
                        2 * if (correctAnswer == 0) 10 else (correctAnswer + 1)
                    )
                ) // random number from <1;20> || <1;minimum 4>
            if (!result.contains(number)) {
                result.add(number)
            }
        }
        answersDrawedEvent.value = result.toList().shuffled().toIntArray()
    }

    fun saveScoreInDatabase(
        player1Nick: String,
        player1Score: Int,
        player2Nick: String,
        player2Score: Int
    ) {
        val scoresToSave = arrayListOf<Score>()
        if (player1Score > player2Score) {
            scoresToSave.add(Score(player1Nick, player1Score))
        } else if (player1Score < player2Score) {
            scoresToSave.add(Score(player2Nick, player2Score))
        } else {
            // draw, save both scores
            scoresToSave.add(Score(player1Nick, player1Score))
            scoresToSave.add(Score(player2Nick, player2Score))
        }

        manageDisposable {
            database.scoreDao()
                .insertAll(scoresToSave)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    saveScoreFinishedEvent.postValue(Unit)
                }
        }
    }

    fun isAnswerCorrect(answer: Int): Boolean {
        return activeEquation.correctAnswer == answer
    }
}
