package com.hexbit.additionandsubtraction.ui.fragment.game.battle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hexbit.additionandsubtraction.data.AppDatabase
import com.hexbit.additionandsubtraction.data.model.Equation
import com.hexbit.additionandsubtraction.data.model.Operation
import com.hexbit.additionandsubtraction.data.model.Score
import com.hexbit.additionandsubtraction.util.base.DisposableViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
        println("draw equation...")
        val valueA = Random.nextInt(0, maxNumber + 1)
        val valueB = Random.nextInt(0, maxNumber + 1)
        var correctAnswer = 0
        var operation = Operation.PLUS_MINUS

        when (Random.nextBoolean()) {
            true -> {
                correctAnswer = valueA + valueB
                operation = Operation.PLUS
            }
            false -> {
                correctAnswer = valueA - valueB
                operation = Operation.MINUS
            }
        }

        val equationToAdd = Equation(
            valueA,
            valueB,
            operation,
            correctAnswer
        )

        if (equationIsValid(equationToAdd)) {
            drawAnswers(equationToAdd.correctAnswer)
            return equationToAdd
        }
        return drawEquation()
    }

    
     // It checks that given equation:
     // - none of input number is equal to 0 (for example: it never draw equation 0+1 or 5-0 etc.)
     // - correctAnswer is always smaller than maxNumber possible number
     // - correctAnswer is equal or greater than 0
     
     //# Sprawdza czy podane równanie spełnia warunki:
     //# - żadna ze zmiennych w równaniu nie jest równa 0
     //# - correctAnswer jest mniejsza od maxNumber danego typu zadania
     //# - correctAnswer jest równa lub większa od 0
     
    private fun equationIsValid(equationToAdd: Equation): Boolean {
        if (equationToAdd.componentA == 0 || equationToAdd.componentB == 0) {
            return false
        }
        if (equationToAdd.correctAnswer > maxNumber) {
            return false
        }
        if (equationToAdd.correctAnswer < 0) {
            return false
        }
        return true
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
