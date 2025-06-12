package com.hexbit.rutmath.ui.fragment.game.divisibility

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hexbit.rutmath.data.model.Equation
import com.hexbit.rutmath.data.model.Operation
import com.hexbit.rutmath.util.base.DisposableViewModel
import kotlin.math.round
import kotlin.random.Random

class DivisibilityGameViewModel : DisposableViewModel() {

    companion object {
        const val EXERCISES_COUNT = 30 // number of exercises in game
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

    private lateinit var args: DivisibilityGameFragmentArgs

    private var currentEquationIndex = -1

    fun getActiveEquation(): LiveData<Equation> = activeEquation

    fun getEndGameEvent(): LiveData<Int> = endGameEvent

    fun getAnswerEvent(): LiveData<AnswerEvent> = answerEvent

    fun init(args: DivisibilityGameFragmentArgs) {
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
            var a = 0
            var b = 0
            var correctAnswer = 0
            var operation = Operation.DIVISIBILITY
            val difficulty = args.exerciseType.difficulty
            var isDivisible = true
            var counter = 0
            if(Random.nextInt(0,2) == 0)
                isDivisible = false
            do {
                counter += 1
                when (difficulty) {
                    1,2,3 -> {
                        if(Random.nextInt(1,3) == 1){
                            a = (Random.nextInt(1,11+(difficulty-1)*5))
                            val dividersList = listOf(3,4)
                            val randomIndex = Random.nextInt(dividersList.size)
                            b = dividersList[randomIndex]
                            if(a % b == 0)
                                correctAnswer = 1
                            else
                                correctAnswer = 0
                        }
                        else{
                            a = (Random.nextInt(1,21+(difficulty-1)*10))
                            val dividersList = listOf(2,5)
                            val randomIndex = Random.nextInt(dividersList.size)
                            b = dividersList[randomIndex]
                            if(a % b == 0)
                                correctAnswer = 1
                            else
                                correctAnswer = 0
                        }
                    }
                    4,5,6 -> {
                        if(Random.nextInt(1,4) > 1){
                            a = (Random.nextInt(1,21+(difficulty-4)*10))
                            val dividersList = listOf(3,4,6,7,8,9)
                            val randomIndex = Random.nextInt(dividersList.size)
                            b = dividersList[randomIndex]
                            if(a % b == 0)
                                correctAnswer = 1
                            else
                                correctAnswer = 0
                        }
                        else{
                            a = (Random.nextInt(1,41+(difficulty-4)*30))
                            val dividersList = listOf(2,5,10)
                            val randomIndex = Random.nextInt(dividersList.size)
                            b = dividersList[randomIndex]
                            if(a % b == 0)
                                correctAnswer = 1
                            else
                                correctAnswer = 0
                        }
                    }
                    7,8,9 ->
                        if(Random.nextInt(1,6) > 1){
                            a = (Random.nextInt(11+(difficulty-7)*10,61+(difficulty-7)*30))
                            val dividersList = listOf(3,4,6,7,8,9)
                            val randomIndex = Random.nextInt(dividersList.size)
                            b = dividersList[randomIndex]
                            if(a % b == 0)
                                correctAnswer = 1
                            else
                                correctAnswer = 0
                        }
                        else{
                            a = (Random.nextInt(31+(difficulty-7)*20,151))
                            val dividersList = listOf(2,5,10)
                            val randomIndex = Random.nextInt(dividersList.size)
                            b = dividersList[randomIndex]
                            if(a % b == 0)
                                correctAnswer = 1
                            else
                                correctAnswer = 0
                        }
                    10 -> {
                        a = (Random.nextInt(51,1000))
                        val dividersList = listOf(3,4,6,7,8,9)
                        val randomIndex = Random.nextInt(dividersList.size)
                        b = dividersList[randomIndex]
                        if(a % b == 0)
                            correctAnswer = 1
                        else
                            correctAnswer = 0
                    }
                }
            }while ((a % b == 0) != isDivisible)
            val equationToAdd = Equation(
                a,
                b,
                operation,
                correctAnswer
            )

            results.contains(equationToAdd).let {
                if (!it) {
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
     * It returns number in range 0..5
     * 0-10% correct answers    - rate 0
     * 11-29% correct answers   - rate 1
     * 30-49% correct answers   - rate 2
     * 50-69% correct answers   - rate 3
     * 70%-89% correct answers  - rate 4
     * 90%-100% correct answers - rate 5
     */

    private fun calculateGameRate(): Int {
        val correctAnswers = equations.map { it.second }.count { it }
        val percent = (correctAnswers.toFloat() / (equations.size).toFloat()) * 100
        return round(percent / 20).toInt()
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