package com.octbit.rutmath.ui.fragment.game.table

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.octbit.rutmath.data.model.Equation
import com.octbit.rutmath.data.model.Operation
import com.octbit.rutmath.ui.fragment.game.normal.NormalGameViewModel
import com.octbit.rutmath.util.base.DisposableViewModel
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.random.Random

class TableGameViewModel : DisposableViewModel() {

    companion object {
        const val EXERCISES_COUNT = 20 // number of exercises in game
        const val MAX_NUMBER = 100 // max answer number
    }

    /**
     * Result of user answer. It can be VALID or INVALID.
     * It is valid only when user input correct value that is equals to activeEquaction.correctAnswer
     */
    enum class AnswerEvent {
        VALID,
        INVALID
    }

    private val equations: ArrayList<Pair<Equation, Boolean>> = arrayListOf()

    private val activeEquation = MutableLiveData<Equation>()

    private val endGameEvent = MutableLiveData<Int>()

    private val answerEvent = MutableLiveData<AnswerEvent>()

    private lateinit var args: TableGameFragmentArgs

    private var currentEquationIndex = -1

    fun getActiveEquation(): LiveData<Equation> = activeEquation

    fun getEndGameEvent(): LiveData<Int> = endGameEvent

    fun getAnswerEvent(): LiveData<AnswerEvent> = answerEvent

    fun init(args: TableGameFragmentArgs) {
        this.args = args
        equations.addAll(drawEquations().map { Pair(it, true) })
        setNextActiveEquation()
    }

    /**
     * Draw equations for game and returns list.
     */

    private fun drawEquations(): List<Equation> {
        val results = arrayListOf<Equation>()
        while (results.size < EXERCISES_COUNT) {
            val a = Random.nextInt(2, sqrt((MAX_NUMBER).toDouble()).roundToInt()+1)
            val b = Random.nextInt(2, sqrt((MAX_NUMBER).toDouble()).roundToInt()+1)
            val correctAnswer = a*b
            val operation = Operation.MULTIPLY
            val equationToAdd = Equation(
                a,
                b,
                operation,
                correctAnswer
            )

            results.contains(equationToAdd).let {
                if (!it or (MAX_NUMBER < (NormalGameViewModel.EXERCISES_COUNT /2))) {
                    results.add(equationToAdd)
                }
            }
        }
        return results
    }


    /**
     * Change active equation to next. If user outplayed all of equations it trigger end game event.
     */

    fun setNextActiveEquation() {
        if (currentEquationIndex + 1 < equations.size) {
            currentEquationIndex++
            activeEquation.value = equations[currentEquationIndex].first
        } else {
            //endgame
            endGameEvent.value = calculateGameRate()
        }
    }

    /**
     * It returns number in range 0..100
     * 0%-100% correct answers => rate 0-100
     */

    private fun calculateGameRate(): Int {
        val correctAnswers = equations.map { it.second }.count { it }
        val percent = (correctAnswers.toFloat() / (equations.size).toFloat()) * 100
        return round(percent).toInt()
    }

    /**
     * Validate if user deliver proper answer to current equation.
     */

    fun validateAnswer(answer: Int) {
        if (equations[currentEquationIndex].first.correctAnswer == answer) {
            answerEvent.value = AnswerEvent.VALID
        } else {
            answerEvent.value = AnswerEvent.INVALID
        }
    }

    /**
     * If user deliver invalid answer current equation should be mark as failed.
     * That will affect on game rate.
     */

    fun markActiveEquationAsFailed() {
        equations[currentEquationIndex] = equations[currentEquationIndex].copy(second = false)
    }
}