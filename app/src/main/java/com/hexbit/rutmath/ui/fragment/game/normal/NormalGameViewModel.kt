package com.hexbit.rutmath.ui.fragment.game.normal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hexbit.rutmath.data.model.Equation
import com.hexbit.rutmath.data.model.Operation
import com.hexbit.rutmath.util.base.DisposableViewModel
import java.lang.Exception
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.random.Random

class NormalGameViewModel : DisposableViewModel() {

    companion object {
        const val EXERCISES_COUNT = 20 // number of exercises in game
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

    private lateinit var args: NormalGameFragmentArgs

    private var currentEquationIndex = -1

    fun getActiveEquation(): LiveData<Equation> = activeEquation

    fun getEndGameEvent(): LiveData<Int> = endGameEvent

    fun getAnswerEvent(): LiveData<AnswerEvent> = answerEvent

    fun init(args: NormalGameFragmentArgs) {
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
            var correctAnswer:Int
            val possibleValues = mutableListOf<Int>()
            var operation:Operation
            val difficulty = args.exerciseType.difficulty

            if (args.exerciseType.operation == Operation.PLUS_MINUS)
                operation = if (Random.nextInt(2) == 0)
                    Operation.PLUS
                else
                    Operation.MINUS
            else if (args.exerciseType.operation == Operation.MULTIPLY_DIVIDE)
                operation = if (Random.nextInt(2) == 0)
                    Operation.MULTIPLY
                else
                    Operation.DIVIDE
            else if (args.exerciseType.operation == Operation.NEGATIVE_PLUS_MINUS)
                operation = if (Random.nextInt(2) == 0)
                    Operation.NEGATIVE_PLUS
                else
                    Operation.NEGATIVE_MINUS
            else
                operation = args.exerciseType.operation


            when (operation) {
                Operation.PLUS -> {
                    a = Random.nextInt(1+(difficulty* 0.1).toInt(), (difficulty*0.9).toInt())
                    for (num in (1+(difficulty * 0.1).toInt())..(difficulty + 1)){
                        if (a + num < difficulty+1)
                            possibleValues.add(num)
                    }
                    if (possibleValues.any())
                        b = possibleValues.random()
                    correctAnswer = a + b
                }
                Operation.MINUS -> {
                    a = Random.nextInt(1+(difficulty* 0.1).toInt(), difficulty + 1)
                    for (num in 1..difficulty + 1){
                        if (a - num > 0)
                            possibleValues.add(num)
                    }
                    if (possibleValues.any())
                        b = possibleValues.random()
                    correctAnswer = a - b
                }
                Operation.MULTIPLY -> {
                    a = Random.nextInt(2, sqrt((difficulty).toDouble()).roundToInt()+1)
                    for (num in 1..(difficulty + 1)){
                        if ((a * num <= difficulty+1) and ((a * num) >= (difficulty* 0.2)))
                            possibleValues.add(num)
                    }
                    if (possibleValues.any())
                        b = possibleValues.random()
                    correctAnswer = a * b
                }
                Operation.DIVIDE -> {
                    b = Random.nextInt(2, sqrt((difficulty).toDouble()).roundToInt()+1)
                    for (num in ((difficulty* 0.2).toInt())..(difficulty+1)){
                        if (num % b == 0)
                            possibleValues.add(num)
                    }
                    if (possibleValues.any())
                        a = possibleValues.random()
                    correctAnswer = a / b
                }

                Operation.NEGATIVE_PLUS -> {
                    a = Random.nextInt(1+(difficulty* 0.1).toInt(), (difficulty*0.9).toInt())
                    for (num in (1+(difficulty * 0.1).toInt())..(difficulty + 1)){
                        if (a + num < difficulty+1)
                            possibleValues.add(num)
                    }
                    if (possibleValues.any())
                        b = possibleValues.random()

                    if (Random.nextBoolean()) {
                        a = -a
                    } else {
                            b = -b
                    }

                    if (Random.nextBoolean()) {
                        if (a>0) b = -b
                        if (b>0) a = -a
                    }


                    correctAnswer = a + b
                }
                Operation.NEGATIVE_MINUS -> {
                    a = Random.nextInt(1+(difficulty* 0.1).toInt(), (difficulty*0.9).toInt())
                    for (num in (1+(difficulty * 0.1).toInt())..(difficulty + 1)){
                        if (a + num < difficulty+1)
                            possibleValues.add(num)
                    }
                    if (possibleValues.any())
                        b = possibleValues.random()

                    if (Random.nextBoolean()) {
                        a = -a
                    } else {
                        b = -b
                    }

                    if (Random.nextBoolean()) {
                        if (a>0) b = -b
                        if (b>0) a = -a
                    }

                    correctAnswer = a - b
                }

                // added code here for multyplication with negative numbers
                Operation.NEGATIVE_MUL -> {
                    a = Random.nextInt(2, sqrt((difficulty).toDouble()).roundToInt()+1)
                    for (num in 1..(difficulty + 1)){
                        if ((a * num <= difficulty+1) and ((a * num) >= (difficulty* 0.2)))
                            possibleValues.add(num)
                    }
                    if (possibleValues.any())
                        b = possibleValues.random()

                    if (Random.nextBoolean()) {
                        a = -a
                    } else {
                        b = -b
                    }

                    if (Random.nextBoolean()) {
                        if (a>0) b = -b
                        if (b>0) a = -a
                    }

                    correctAnswer = a * b
                }

                Operation.NEGATIVE_DIV -> {
                    b = Random.nextInt(2, sqrt((difficulty).toDouble()).roundToInt()+1)
                    for (num in ((difficulty* 0.2).toInt())..(difficulty+1)){
                        if (num % b == 0)
                            possibleValues.add(num)
                    }
                    if (possibleValues.any())
                        a = possibleValues.random()

                    if (Random.nextBoolean()) {
                        a = -a
                    } else {
                        b = -b
                    }

                    if (Random.nextBoolean()) {
                        if (a>0) b = -b
                        if (b>0) a = -a
                    }

                    correctAnswer = a / b
                }

                else -> throw Exception("Operation not implemented in this View")
            }

            val equationToAdd = Equation(
                a,
                b,
                operation,
                correctAnswer
            )

            results.contains(equationToAdd).let {
                if (!it or (difficulty < ((EXERCISES_COUNT/2)+1))) {
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