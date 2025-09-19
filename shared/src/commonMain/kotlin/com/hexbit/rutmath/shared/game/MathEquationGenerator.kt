package com.octbit.rutmath.shared.game

import com.octbit.rutmath.shared.model.Equation
import com.octbit.rutmath.shared.model.EquationUnits
import com.octbit.rutmath.shared.model.ExerciseType
import com.octbit.rutmath.shared.model.Operation
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * Implementation of EquationGenerator that creates mathematical equations.
 */
class MathEquationGenerator : EquationGenerator {
    
    companion object {
        // Units for conversions
        val UNITS_TIME = listOf("d", "h", "min", "sec")
        val RATIO_TIME = listOf(24, 60, 60)
        val UNITS_LENGTH = listOf("km", "m", "dm", "cm", "mm")
        val RATIO_LENGTH = listOf(1000, 10, 10, 10)
        val UNITS_WEIGHT = listOf("t", "kg", "dag", "g")
        val RATIO_WEIGHT = listOf(1000, 100, 10)
        val UNITS_SURFACE = listOf("km²", "ha", "a", "m²", "dm²", "cm²", "mm²")
        val RATIO_SURFACE = listOf(100, 100, 100, 100, 100, 100)
    }
    
    override fun generateEquation(operation: Operation, difficulty: Int): Equation {
        var a = 0
        var b = 0
        var correctAnswer: Int
        val possibleValues = mutableListOf<Int>()
        
        val actualOperation = when (operation) {
            Operation.PLUS_MINUS -> if (Random.nextInt(2) == 0) Operation.PLUS else Operation.MINUS
            Operation.MULTIPLY_DIVIDE -> if (Random.nextInt(2) == 0) Operation.MULTIPLY else Operation.DIVIDE
            Operation.NEGATIVE_PLUS_MINUS -> if (Random.nextInt(2) == 0) Operation.NEGATIVE_PLUS else Operation.NEGATIVE_MINUS
            Operation.NEGATIVE_MUL_DIV -> if (Random.nextInt(2) == 0) Operation.NEGATIVE_MUL else Operation.NEGATIVE_DIV
            else -> operation
        }
        
        when (actualOperation) {
            Operation.PLUS -> {
                a = Random.nextInt(1 + (difficulty * 0.1).toInt(), (difficulty * 0.9).toInt())
                for (num in (1 + (difficulty * 0.1).toInt())..(difficulty + 1)) {
                    if (a + num < difficulty + 1) possibleValues.add(num)
                }
                if (possibleValues.any()) b = possibleValues.random()
                correctAnswer = a + b
            }
            
            Operation.MINUS -> {
                a = Random.nextInt(1 + (difficulty * 0.1).toInt(), difficulty + 1)
                for (num in 1..difficulty + 1) {
                    if (a - num > 0) possibleValues.add(num)
                }
                if (possibleValues.any()) b = possibleValues.random()
                correctAnswer = a - b
            }
            
            Operation.MULTIPLY -> {
                a = Random.nextInt(2, sqrt(difficulty.toDouble()).roundToInt() + 1)
                for (num in 1..(difficulty + 1)) {
                    if ((a * num <= difficulty + 1) && ((a * num) >= (difficulty * 0.2))) {
                        possibleValues.add(num)
                    }
                }
                if (possibleValues.any()) b = possibleValues.random()
                correctAnswer = a * b
            }
            
            Operation.DIVIDE -> {
                b = Random.nextInt(2, sqrt(difficulty.toDouble()).roundToInt() + 1)
                for (num in ((difficulty * 0.2).toInt())..(difficulty + 1)) {
                    if (num % b == 0) possibleValues.add(num)
                }
                if (possibleValues.any()) a = possibleValues.random()
                correctAnswer = a / b
            }
            
            Operation.NEGATIVE_PLUS -> {
                a = Random.nextInt(1 + (difficulty * 0.1).toInt(), (difficulty * 0.9).toInt())
                for (num in (1 + (difficulty * 0.1).toInt())..(difficulty + 1)) {
                    if (a + num < difficulty + 1) possibleValues.add(num)
                }
                if (possibleValues.any()) b = possibleValues.random()
                
                if (Random.nextBoolean()) {
                    a = -a
                } else {
                    b = -b
                }
                correctAnswer = a + b
            }
            
            Operation.NEGATIVE_MINUS -> {
                a = Random.nextInt(1 + (difficulty * 0.1).toInt(), difficulty + 1)
                for (num in 1..difficulty + 1) {
                    if (a - num > -difficulty) possibleValues.add(num)
                }
                if (possibleValues.any()) b = possibleValues.random()
                
                if (Random.nextBoolean()) {
                    a = -a
                } else {
                    b = -b
                }
                correctAnswer = a - b
            }
            
            Operation.NEGATIVE_MUL -> {
                a = Random.nextInt(2, sqrt(difficulty.toDouble()).roundToInt() + 1)
                for (num in 1..(difficulty + 1)) {
                    if ((a * num <= difficulty + 1) && ((a * num) >= (difficulty * 0.2))) {
                        possibleValues.add(num)
                    }
                }
                if (possibleValues.any()) b = possibleValues.random()
                
                if (Random.nextBoolean()) {
                    a = -a
                } else {
                    b = -b
                }
                
                if (Random.nextBoolean()) {
                    if (a > 0) b = -b
                    if (b > 0) a = -a
                }
                correctAnswer = a * b
            }
            
            Operation.NEGATIVE_DIV -> {
                b = Random.nextInt(2, sqrt(difficulty.toDouble()).roundToInt() + 1)
                for (num in ((difficulty * 0.2).toInt())..(difficulty + 1)) {
                    if (num % b == 0) possibleValues.add(num)
                }
                if (possibleValues.any()) a = possibleValues.random()
                
                if (Random.nextBoolean()) {
                    a = -a
                } else {
                    b = -b
                }
                
                if (Random.nextBoolean()) {
                    if (a > 0) b = -b
                    if (b > 0) a = -a
                }
                correctAnswer = a / b
            }
            
            Operation.DIVISIBILITY -> {
                val isDivisible = Random.nextInt(0, 2) == 0
                
                when (difficulty) {
                    in 1..3 -> {
                        if (Random.nextInt(1, 3) == 1) {
                            a = Random.nextInt(1, 11 + (difficulty - 1) * 5)
                            val dividersList = listOf(3, 4)
                            b = dividersList.random()
                        } else {
                            a = Random.nextInt(1, 21 + (difficulty - 1) * 10)
                            val dividersList = listOf(2, 5)
                            b = dividersList.random()
                        }
                    }
                    in 4..6 -> {
                        if (Random.nextInt(1, 4) > 1) {
                            a = Random.nextInt(1, 21 + (difficulty - 4) * 10)
                            val dividersList = listOf(3, 4, 6, 7, 8, 9)
                            b = dividersList.random()
                        } else {
                            a = Random.nextInt(1, 41 + (difficulty - 4) * 30)
                            val dividersList = listOf(2, 5, 10)
                            b = dividersList.random()
                        }
                    }
                    in 7..9 -> {
                        if (Random.nextInt(1, 6) > 1) {
                            a = Random.nextInt(11 + (difficulty - 7) * 10, 61 + (difficulty - 7) * 30)
                            val dividersList = listOf(3, 4, 6, 7, 8, 9)
                            b = dividersList.random()
                        } else {
                            a = Random.nextInt(31 + (difficulty - 7) * 20, 151)
                            val dividersList = listOf(2, 5, 10)
                            b = dividersList.random()
                        }
                    }
                    10 -> {
                        a = Random.nextInt(51, 1000)
                        val dividersList = listOf(3, 4, 6, 7, 8, 9)
                        b = dividersList.random()
                    }
                }
                
                // Adjust to get desired divisibility
                if ((a % b == 0) != isDivisible) {
                    if (isDivisible) {
                        a = (a / b) * b
                    } else {
                        a = a + 1
                    }
                }
                
                correctAnswer = if (a % b == 0) 1 else 0
            }
            
            else -> throw IllegalArgumentException("Operation not implemented: $actualOperation")
        }
        
        return Equation(a, b, actualOperation, correctAnswer)
    }
    
    override fun generateUnitsEquation(operation: Operation, difficulty: Int): EquationUnits {
        val shiftDifficulty = difficulty
        val numberDifficulty: Int
        val units: List<String>
        val ratio: List<Int>
        
        val actualOperation = when (operation) {
            Operation.UNITS_ALL -> {
                val list = listOf(
                    Operation.UNITS_TIME,
                    Operation.UNITS_LENGTH,
                    Operation.UNITS_WEIGHT,
                    Operation.UNITS_SURFACE
                )
                list.random()
            }
            else -> operation
        }
        
        when (actualOperation) {
            Operation.UNITS_TIME -> {
                units = UNITS_TIME
                ratio = RATIO_TIME
                numberDifficulty = 7
            }
            Operation.UNITS_LENGTH -> {
                units = UNITS_LENGTH
                ratio = RATIO_LENGTH
                numberDifficulty = 25 * shiftDifficulty
            }
            Operation.UNITS_WEIGHT -> {
                units = UNITS_WEIGHT
                ratio = RATIO_WEIGHT
                numberDifficulty = 20 * shiftDifficulty
            }
            Operation.UNITS_SURFACE -> {
                units = UNITS_SURFACE
                ratio = RATIO_SURFACE
                numberDifficulty = 15 * shiftDifficulty
            }
            else -> throw IllegalArgumentException("Units operation not implemented: $actualOperation")
        }
        
        val aId = Random.nextInt(units.size)
        val choiceList = ((max(0, aId - shiftDifficulty)..min(aId + shiftDifficulty, units.size - 1)) - aId)
        val answerId = choiceList.random()
        val difference = answerId - aId
        
        var conversionRatio = 1
        var ratioId: Int
        val a: Int
        val answer: Int
        
        if (difference > 0) {
            ratioId = aId
            for (i in 1..abs(difference)) {
                conversionRatio *= ratio[ratioId]
                ratioId++
            }
            a = Random.nextInt(1, numberDifficulty + 1)
            answer = a * conversionRatio
        } else {
            ratioId = aId - 1
            for (i in 1..abs(difference)) {
                conversionRatio *= ratio[ratioId]
                ratioId--
            }
            a = Random.nextInt(1, numberDifficulty + 1) * conversionRatio
            answer = a / conversionRatio
        }
        
        return EquationUnits(a, aId, actualOperation, answer, answerId)
    }
    
    override fun generateAnswers(correctAnswer: Int, count: Int): List<Int> {
        val result = arrayListOf<Int>()
        result.add(correctAnswer)
        
        while (result.size < count) {
            val number = Random.nextInt(
                1,
                2 * if (correctAnswer == 0) 10 else (correctAnswer + 1)
            )
            if (!result.contains(number)) {
                result.add(number)
            }
        }
        
        return result.shuffled()
    }
    
    override fun generateEquations(exerciseType: ExerciseType, exerciseCount: Int): List<Equation> {
        val results = arrayListOf<Equation>()
        var attempts = 0
        val maxAttempts = exerciseCount * 10
        
        while (results.size < exerciseCount && attempts < maxAttempts) {
            attempts++
            try {
                val equation = generateEquation(exerciseType.operation, exerciseType.difficulty)
                if (!results.contains(equation) || exerciseType.difficulty < (exerciseCount / 2 + 1)) {
                    results.add(equation)
                }
            } catch (e: Exception) {
                // Continue trying if equation generation fails
                continue
            }
        }
        
        return results
    }
    
    override fun generateUnitsEquations(exerciseType: ExerciseType, exerciseCount: Int): List<EquationUnits> {
        val results = arrayListOf<EquationUnits>()
        var attempts = 0
        val maxAttempts = exerciseCount * 10
        
        while (results.size < exerciseCount && attempts < maxAttempts) {
            attempts++
            try {
                val equation = generateUnitsEquation(exerciseType.operation, exerciseType.difficulty)
                if (!results.contains(equation)) {
                    results.add(equation)
                }
            } catch (e: Exception) {
                // Continue trying if equation generation fails
                continue
            }
        }
        
        return results
    }
} 
