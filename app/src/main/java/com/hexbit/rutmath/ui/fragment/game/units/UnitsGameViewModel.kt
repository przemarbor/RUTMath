package com.hexbit.rutmath.ui.fragment.game.units

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hexbit.rutmath.data.model.EquationUnits
import com.hexbit.rutmath.data.model.Operation
import com.hexbit.rutmath.util.base.DisposableViewModel
import java.lang.Exception
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.random.Random

class UnitsGameViewModel : DisposableViewModel() {

    companion object {
        const val EXERCISES_COUNT = 20 // number of exercises in game
        // Time Units names are later being loaded from Strings
        var UNITS_TIME = listOf("d","h","min","sec")
        var RATIO_TIME = listOf(24,60,60)
        var UNITS_LENGTH = listOf("km","m","dm","cm","mm")
        var RATIO_LENGTH = listOf(1000,10,10,10)
        var UNITS_WEIGHT = listOf("t","kg","dag","g")
        var RATIO_WEIGHT = listOf(1000,10,10)
        var UNITS_SURFACE = listOf("km^2","ha","a","m^2","dm^2","cm^2","mm^2")
        var RATIO_SURFACE = listOf(100,100,100,100,100,100)
    }

    /**
     * Result of user answer. It can be VALID or INVALID.
     * It is valid only when user input correct value that is equals to activeEquaction.correctAnswer
     */
    enum class AnswerEvent {
        VALID,
        INVALID
    }

    private val equations: ArrayList<Pair<EquationUnits, Boolean>> = arrayListOf()

    private val activeEquation = MutableLiveData<EquationUnits>()

    private val endGameEvent = MutableLiveData<Int>()

    private val answerEvent = MutableLiveData<AnswerEvent>()

    private lateinit var args: UnitsGameFragmentArgs

    private var currentEquationIndex = -1

    fun getActiveEquation(): LiveData<EquationUnits> = activeEquation

    fun getEndGameEvent(): LiveData<Int> = endGameEvent

    fun getAnswerEvent(): LiveData<AnswerEvent> = answerEvent

    fun init(args: UnitsGameFragmentArgs) {
        this.args = args
        equations.addAll(drawEquations().map { Pair(it, true) })
        setNextActiveEquation()
    }

    /**
     * Draw equations for game and returns list.
     *
     */
    private fun drawEquations(): List<EquationUnits> {
        val results = arrayListOf<EquationUnits>()
        while (results.size < UnitsGameViewModel.EXERCISES_COUNT) {
            var a:Int
            var aUnitId:Int
            var correctAnswer:Int
            var answerUnitId:Int
            var operation:Operation

            if (args.exerciseType.operation == Operation.UNITS_ALL) {
                val list = listOf(
                    Operation.UNITS_TIME,
                    Operation.UNITS_LENGTH,
                    Operation.UNITS_WEIGHT,
                    Operation.UNITS_SURFACE
                )
                val randomIndex = Random.nextInt(list.size);
                operation = list[randomIndex]
            }
            else
                operation = args.exerciseType.operation

            when (operation) {
                Operation.UNITS_TIME -> {
                    aUnitId = Random.nextInt(UNITS_TIME.size)

                    val list = ((max(0,aUnitId-1) .. (min(aUnitId+1,UNITS_TIME.size-1))) - aUnitId)
                    val randomIndex = Random.nextInt(list.size);
                    answerUnitId = list[randomIndex]

                    val diff = answerUnitId - aUnitId
                    var scale = 60.0
                    if ((answerUnitId == 0) or (aUnitId == 0))
                        scale = 24.0
                    a = Random.nextInt(1, args.exerciseType.maxNumber * 5 + 1)
                    if (diff < 0)
                        a *= (scale).pow(-diff).toInt()
                    correctAnswer = (a * (scale).pow(diff)).toInt()
                }
                Operation.UNITS_LENGTH -> {
                    aUnitId = Random.nextInt(UNITS_LENGTH.size)

                    val list = ((max(0,aUnitId-1) .. (min(aUnitId+1,UNITS_LENGTH.size-1))) - aUnitId)
                    val randomIndex = Random.nextInt(list.size);
                    answerUnitId = list[randomIndex]

                    val diff = answerUnitId - aUnitId
                    var scale = 10.0
                    if ((answerUnitId == 0) or (aUnitId == 0))
                        scale = 1000.0
                    a = Random.nextInt(1, args.exerciseType.maxNumber * args.exerciseType.maxNumber * 10 + 1)
                    if (diff < 0)
                        a *= (scale).pow(-diff).toInt()
                    correctAnswer = (a * (scale).pow(diff)).toInt()
                }
                Operation.UNITS_WEIGHT -> {
                    aUnitId = Random.nextInt(UNITS_WEIGHT.size)

                    val list = ((max(0,aUnitId-1) .. (min(aUnitId+1,UNITS_WEIGHT.size-1))) - aUnitId)
                    val randomIndex = Random.nextInt(list.size);
                    answerUnitId = list[randomIndex]

                    val diff = answerUnitId - aUnitId
                    var scale = 10.0
                    if ((answerUnitId == 0) or (aUnitId == 0))
                        scale = 1000.0
                    a = Random.nextInt(1, args.exerciseType.maxNumber * args.exerciseType.maxNumber * 10 + 1)
                    if (diff < 0)
                        a *= (scale).pow(-diff).toInt()
                    correctAnswer = (a * (scale).pow(diff)).toInt()
                }
                Operation.UNITS_SURFACE -> {
                    aUnitId = Random.nextInt(UNITS_SURFACE.size)

                    val list = ((max(0,aUnitId-1) .. (min(aUnitId+1,UNITS_SURFACE.size-1))) - aUnitId)
                    val randomIndex = Random.nextInt(list.size);
                    answerUnitId = list[randomIndex]

                    val diff = answerUnitId - aUnitId
                    val scale = 100.0
                    a = Random.nextInt(1, args.exerciseType.maxNumber * args.exerciseType.maxNumber * 10 + 1)
                    if (diff < 0)
                        a *= (scale).pow(-diff).toInt()
                    correctAnswer = (a * (scale).pow(diff)).toInt()
                }
                else -> throw Exception("Operation not implemented in this View")
            }

            val equationToAdd = EquationUnits(
                a,
                aUnitId,
                operation,
                correctAnswer,
                answerUnitId
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
        val percent =
            ((correctAnswers.toFloat() / (equations.size).toFloat()) * 100).toInt()
        return percent / 20
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

    fun createHelpText(operation: Operation):String
    {
        var res = ""
        var c = 0
        val units:List<String>
        val ratio:List<Int>
        when(operation){
            Operation.UNITS_TIME -> {
                units = UNITS_TIME
                ratio = RATIO_TIME
            }
            Operation.UNITS_LENGTH -> {
                units = UNITS_LENGTH
                ratio = RATIO_LENGTH
            }
            Operation.UNITS_WEIGHT -> {
                units = UNITS_WEIGHT
                ratio = RATIO_WEIGHT
            }
            Operation.UNITS_SURFACE -> {
                units = UNITS_SURFACE
                ratio = RATIO_SURFACE
            }
            else -> throw Exception("Invalid operation!")
        }
        while (c < ratio.size){
            res = res.plus("1${units[c]} = ${ratio[c]}${units[c+1]}\n")
            c+=1
        }
        return res
    }
}