package com.hexbit.rutmath.ui.fragment.game.normal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hexbit.rutmath.data.model.Equation
import com.hexbit.rutmath.data.model.Operation
import com.hexbit.rutmath.util.base.DisposableViewModel
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.random.Random

class NormalGameViewModel : DisposableViewModel() {

    companion object {
        const val EXERCISES_COUNT = 10 // number of exercises in game
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
     *
     * It is ensure that all of equations do not repeat themselves.
     * It can throw StackOverflowException when EXERCISES_COUNT < Sigma(args.maxNumber) in
     * case when args.operation = Operation.MINUS.
     *
     * In other words: it is mathematically impossible
     * to create more equations with minus(-) operator for numbers (for example) in range 0-5
     * (args.maxNumber = 5).
     * Because we have only 10 possibly operations [like 5-4, 5-3,5-2,5-1,4-3,4-2,4-1,3-2,3-1,2-1]
     * so it is Sigma(sum of) of args.maxNumber.
     */
    //# Losuje równania dla gry i zwraca w postaci listy.
    //# Zapewnia niepowtarzalność elementów w liście.
    //# Może zablokować program przez StackOverflowException w momencie gdy zmienna
    //# EXERCISES_COUNT jest mniejsza od sumy (args.Number) w przypadku gdy operacja dla jakiej
    //# losowane są liczby jest = Operator.MINUS. Dzieje się tak dlatego, że
    //# matematycznie nie jest możliwe stworzenie danej liczby (EXERCISES_COUNT) równań i pętla
    //# nie zostanie ukończona przez co program się zawiesi.

    private fun drawEquations(): List<Equation> {
        val results = arrayListOf<Equation>()
        while (results.size < EXERCISES_COUNT) {
            var a = 0
            var b = 0
            var correctAnswer = 0
            var operation = Operation.PLUS
            val possibleValues = mutableListOf<Int>()
            when (args.exerciseType.operation) {
                Operation.PLUS -> {
                    a = Random.nextInt(1, args.exerciseType.maxNumber + 1)
                    for (num in 1..args.exerciseType.maxNumber + 1){
                        if (a + num < args.exerciseType.maxNumber+1)
                            possibleValues.add(num)
                    }
                    if (possibleValues.any())
                        b = possibleValues.random()
                    correctAnswer = a + b
                    operation = Operation.PLUS
                }
                Operation.MINUS -> {
                    a = Random.nextInt(1, args.exerciseType.maxNumber + 1)
                    for (num in 1..args.exerciseType.maxNumber + 1){
                        if (a - num > 0)
                            possibleValues.add(num)
                    }
                    if (possibleValues.any())
                        b = possibleValues.random()
                    correctAnswer = a - b
                    operation = Operation.MINUS
                }
                Operation.PLUS_MINUS -> {
                    when (Random.nextBoolean()) {
                        true -> {
                            a = Random.nextInt(1, args.exerciseType.maxNumber + 1)
                            for (num in 1..args.exerciseType.maxNumber + 1){
                                if (a + num < args.exerciseType.maxNumber+1)
                                    possibleValues.add(num)
                            }
                            if (possibleValues.any())
                                b = possibleValues.random()
                            correctAnswer = a + b
                            operation = Operation.PLUS
                        }
                        false -> {
                            a = Random.nextInt(1, args.exerciseType.maxNumber + 1)
                            for (num in 1..args.exerciseType.maxNumber + 1){
                                if (a - num > 0)
                                    possibleValues.add(num)
                            }
                            if (possibleValues.any())
                                b = possibleValues.random()
                            correctAnswer = a - b
                            operation = Operation.MINUS
                        }
                    }
                }
                Operation.MULTIPLY -> {
                    a = Random.nextInt(1, sqrt((args.exerciseType.maxNumber).toDouble()).roundToInt()+1)
                    for (num in 1..args.exerciseType.maxNumber + 1){
                        if (a * num <= args.exerciseType.maxNumber+1)
                            possibleValues.add(num)
                    }
                    if (possibleValues.any())
                        b = possibleValues.random()
                    correctAnswer = a * b
                    operation = Operation.MULTIPLY
                }
                Operation.DIVIDE -> {
                    b = Random.nextInt(1, sqrt((args.exerciseType.maxNumber).toDouble()).roundToInt()+1)
                    for (num in 1..args.exerciseType.maxNumber+1){
                        if (num % b == 0)
                            possibleValues.add(num)
                    }
                    if (possibleValues.any())
                        a = possibleValues.random()
                    correctAnswer = a / b
                    operation = Operation.DIVIDE
                }
                Operation.MULTIPLY_DIVIDE -> {
                    when (Random.nextBoolean()) {
                        true -> {
                            a = Random.nextInt(1, sqrt((args.exerciseType.maxNumber).toDouble()).roundToInt()+1)
                            for (num in 1..args.exerciseType.maxNumber + 1){
                                if (a * num <= args.exerciseType.maxNumber+1)
                                    possibleValues.add(num)
                            }
                            if (possibleValues.any())
                                b = possibleValues.random()
                            correctAnswer = a * b
                            operation = Operation.MULTIPLY
                        }
                        false -> {
                            b = Random.nextInt(1, sqrt((args.exerciseType.maxNumber).toDouble()).roundToInt()+1)
                            for (num in 1..args.exerciseType.maxNumber+1){
                                if (num % b == 0)
                                    possibleValues.add(num)
                            }
                            if (possibleValues.any())
                                a = possibleValues.random()
                            correctAnswer = a / b
                            operation = Operation.DIVIDE
                        }
                    }
                }
            }

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
    //# Zmienia aktywne zadanie na następne. Jeżeli wszystkie zadania zostały ograne wówczas
    //# wywołuje event końca gry.

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
     //# Zwraca liczbę w zakresie <0, 5> jako wynik rozgrywki gracza.
    private fun calculateGameRate(): Int {
        val correctAnswers = equations.map { it.second }.filter { it }.count()
        val percent =
            ((correctAnswers.toFloat() / (equations.size).toFloat()) * 100).toInt()
        return percent / 20
    }

    /**
     * Validate if user deliver proper answer to current equation.
     */
     //# Sprawdza czy podana odpowiedź jest poprawna

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
     //# Jeżeli odpowiedź jest niepoprawna to ta metoda zaznacza aktywne zadanie jako niewykonane,
     //# wpłynie to potem na ocenę uzyskaną przez gracza.

    fun markActiveEquationAsFailed() {
        equations[currentEquationIndex] = equations[currentEquationIndex].copy(second = false)
    }
}