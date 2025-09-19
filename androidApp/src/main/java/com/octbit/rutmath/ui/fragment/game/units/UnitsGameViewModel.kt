package com.octbit.rutmath.ui.fragment.game.units

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.octbit.rutmath.data.model.EquationUnits
import com.octbit.rutmath.data.model.Operation
import com.octbit.rutmath.util.base.DisposableViewModel
import java.lang.Exception
import kotlin.math.max
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.round
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
        var RATIO_WEIGHT = listOf(1000,100,10)
        var UNITS_SURFACE = listOf("km²","ha","a","m²","dm²","cm²","mm²")
        var RATIO_SURFACE = listOf(100,100,100,100,100,100)
    }

    /**
     * Result of user answer. It can be VALID or INVALID.
     * It is valid only when user input correct value that is equals to activeEquation.correctAnswer
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
     */
    private fun drawEquations(): List<EquationUnits> {
        val results = arrayListOf<EquationUnits>()
        while (results.size < UnitsGameViewModel.EXERCISES_COUNT) {
            var a:Int
            var aId:Int
            var answer:Int
            var answerId:Int
            var ratio = 1
            var ratioId:Int
            var operation:Operation
            val shiftDifficulty = args.exerciseType.difficulty //(1..2)
            val numberDifficulty:Int
            val UNITS:List<String>
            val RATIO:List<Int>

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
                    UNITS = UNITS_TIME
                    RATIO = RATIO_TIME
                    numberDifficulty = 7
                }
                Operation.UNITS_LENGTH -> {
                    UNITS = UNITS_LENGTH
                    RATIO = RATIO_LENGTH
                    numberDifficulty = 25 * shiftDifficulty
                }
                Operation.UNITS_WEIGHT -> {
                    UNITS = UNITS_WEIGHT
                    RATIO = RATIO_WEIGHT
                    numberDifficulty = 20 * shiftDifficulty
                }
                Operation.UNITS_SURFACE -> {
                    UNITS = UNITS_SURFACE
                    RATIO = RATIO_SURFACE
                    numberDifficulty = 15 * shiftDifficulty
                }
                else -> throw Exception("Operation not implemented in this View")
            }

            aId = Random.nextInt(UNITS.size)
            val choiceList = ((max(0,aId-shiftDifficulty) .. min(aId+shiftDifficulty, UNITS.size-1)) - aId)
            answerId = choiceList[Random.nextInt(choiceList.size)]
            val difference = answerId - aId

            if (difference > 0){
                ratioId = aId
                for (i in 1..abs(difference)) {
                    ratio *= RATIO[ratioId]
                    ratioId++
                }
                a = Random.nextInt(1, numberDifficulty + 1)
                answer = a * ratio
            }
            else{
                ratioId = aId-1
                for (i in 1..abs(difference)) {
                    ratio *= RATIO[ratioId]
                    ratioId--
                }
                a = Random.nextInt(1, numberDifficulty + 1) * ratio
                answer = a / ratio
            }

            val equationToAdd = EquationUnits(
                a,
                aId,
                operation,
                answer,
                answerId
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
