package com.hexbit.additionandsubtraction.ui.fragment.game.normal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hexbit.additionandsubtraction.data.model.Equation
import com.hexbit.additionandsubtraction.data.model.Operation
import com.hexbit.additionandsubtraction.util.base.DisposableViewModel
import kotlin.random.Random

class NormalGameViewModel : DisposableViewModel() {

    companion object {
        const val EXERCISES_COUNT = 4 // number of exercises in game
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
            val valueA = Random.nextInt(0, args.exerciseType.maxNumber + 1)
            val valueB = Random.nextInt(0, args.exerciseType.maxNumber + 1)
            var correctAnswer = 0
            var operation = Operation.PLUS
            when (args.exerciseType.operation) {
                Operation.PLUS -> {
                    correctAnswer = valueA + valueB
                    operation = Operation.PLUS
                }
                Operation.MINUS -> {
                    correctAnswer = valueA - valueB
                    operation = Operation.MINUS
                }
                Operation.PLUS_MINUS -> {
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
                }
            }

            val equationToAdd = Equation(
                valueA,
                valueB,
                operation,
                correctAnswer
            )

            if (equationIsValid(equationToAdd)) {
                results.contains(equationToAdd).let {
                    if (!it) {
                        results.add(equationToAdd)
                    }
                }
            }
        }
        return results
    }

    /**
     * It checks that given equation:
     * - none of input number is equal to 0 (for example: it never draw equation 0+1 or 5-0 etc.)
     * - correctAnswer is always smaller than maxNumber of exerciseType
     * - correctAnswer is equal or greater than 0
     */
     //# Sprawdza czy podane równanie spełnia warunki:
     //# - żadna ze zmiennych w równaniu nie jest równa 0
     //# - correctAnswer jest mniejsza od maxNumber danego typu zadania
     //# - correctAnswer jest równa lub większa od 0
     
    private fun equationIsValid(equationToAdd: Equation): Boolean {
        if (equationToAdd.componentA == 0 || equationToAdd.componentB == 0) {
            return false
        }
        if (equationToAdd.correctAnswer > args.exerciseType.maxNumber) {
            return false
        }
        if (equationToAdd.correctAnswer < 0) {
            return false
        }
        return true
    }

    /**
     * Change active equation to next. If user outplayed all of equations it trigger end game event.
     *
     * # Zmienia aktywne zadanie na następne. Jeżeli wszystkie zadania zostały ograne wówczas
     * # wywołuje event końca gry.
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
     *
     * # Zwraca liczbę w zakresie <0, 5> jako wynik rozgrywki gracza.
     *
     */
    private fun calculateGameRate(): Int {
        val correctAnswers = equations.map { it.second }.filter { it }.count()
        val percent =
            ((correctAnswers.toFloat() / (equations.size).toFloat()) * 100).toInt()
        return percent / 20
    }

    /**
     * Validate if user deliver proper answer to current equation.
     *
     * # Sprawdza czy podana odpowiedź jest poprawna
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
     *
     * # Jeżeli odpowiedź jest niepoprawna to ta metoda zaznacza aktywne zadanie jako niewykonane,
     * # wpłynie to potem na ocenę uzyskaną przez gracza.
     */
    fun markActiveEquationAsFailed() {
        equations[currentEquationIndex] = equations[currentEquationIndex].copy(second = false)
    }
}
