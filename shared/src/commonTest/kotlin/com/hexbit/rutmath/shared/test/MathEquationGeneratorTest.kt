package com.hexbit.rutmath.shared.test

import com.hexbit.rutmath.shared.game.MathEquationGenerator
import com.hexbit.rutmath.shared.model.ExerciseType
import com.hexbit.rutmath.shared.model.Operation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Unit tests for MathEquationGenerator.
 */
class MathEquationGeneratorTest {
    
    private val generator = MathEquationGenerator()
    
    @Test
    fun testGenerateEquation_Plus() {
        val equation = generator.generateEquation(Operation.PLUS, 10)
        
        assertNotNull(equation)
        assertEquals(Operation.PLUS, equation.operation)
        assertEquals(equation.componentA + equation.componentB, equation.correctAnswer)
        assertTrue(equation.correctAnswer <= 11) // difficulty + 1
    }
    
    @Test
    fun testGenerateEquation_Minus() {
        val equation = generator.generateEquation(Operation.MINUS, 10)
        
        assertNotNull(equation)
        assertEquals(Operation.MINUS, equation.operation)
        assertEquals(equation.componentA - equation.componentB, equation.correctAnswer)
        assertTrue(equation.correctAnswer > 0) // Should be positive
    }
    
    @Test
    fun testGenerateEquation_Multiply() {
        val equation = generator.generateEquation(Operation.MULTIPLY, 20)
        
        assertNotNull(equation)
        assertEquals(Operation.MULTIPLY, equation.operation)
        assertEquals(equation.componentA * equation.componentB, equation.correctAnswer)
        assertTrue(equation.correctAnswer <= 21) // difficulty + 1
    }
    
    @Test
    fun testGenerateEquation_Divide() {
        val equation = generator.generateEquation(Operation.DIVIDE, 20)
        
        assertNotNull(equation)
        assertEquals(Operation.DIVIDE, equation.operation)
        assertEquals(equation.componentA / equation.componentB, equation.correctAnswer)
        assertEquals(0, equation.componentA % equation.componentB) // Should divide evenly
    }
    
    @Test
    fun testGenerateEquation_Divisibility() {
        val equation = generator.generateEquation(Operation.DIVISIBILITY, 5)
        
        assertNotNull(equation)
        assertEquals(Operation.DIVISIBILITY, equation.operation)
        assertTrue(equation.correctAnswer == 0 || equation.correctAnswer == 1) // Binary result
        
        val isDivisible = equation.componentA % equation.componentB == 0
        val expectedAnswer = if (isDivisible) 1 else 0
        assertEquals(expectedAnswer, equation.correctAnswer)
    }
    
    @Test
    fun testGenerateUnitsEquation_Time() {
        val equation = generator.generateUnitsEquation(Operation.UNITS_TIME, 2)
        
        assertNotNull(equation)
        assertEquals(Operation.UNITS_TIME, equation.operation)
        assertTrue(equation.componentAUnitId in 0..3) // Time units indices
        assertTrue(equation.answerUnitId in 0..3)
        assertTrue(equation.componentA > 0)
        assertTrue(equation.correctAnswer > 0)
    }
    
    @Test
    fun testGenerateUnitsEquation_Length() {
        val equation = generator.generateUnitsEquation(Operation.UNITS_LENGTH, 3)
        
        assertNotNull(equation)
        assertEquals(Operation.UNITS_LENGTH, equation.operation)
        assertTrue(equation.componentAUnitId in 0..4) // Length units indices
        assertTrue(equation.answerUnitId in 0..4)
        assertTrue(equation.componentA > 0)
        assertTrue(equation.correctAnswer > 0)
    }
    
    @Test
    fun testGenerateAnswers() {
        val correctAnswer = 42
        val answers = generator.generateAnswers(correctAnswer, 4)
        
        assertEquals(4, answers.size)
        assertTrue(answers.contains(correctAnswer))
        assertTrue(answers.toSet().size == 4) // All unique
    }
    
    @Test
    fun testGenerateEquations() {
        val exerciseType = ExerciseType(
            operation = Operation.PLUS,
            difficulty = 10,
            userNick = "Test",
            isUnlocked = true
        )
        
        val equations = generator.generateEquations(exerciseType, 5)
        
        assertEquals(5, equations.size)
        equations.forEach { equation ->
            assertEquals(Operation.PLUS, equation.operation)
            assertEquals(equation.componentA + equation.componentB, equation.correctAnswer)
        }
    }
    
    @Test
    fun testGenerateUnitsEquations() {
        val exerciseType = ExerciseType(
            operation = Operation.UNITS_TIME,
            difficulty = 2,
            userNick = "Test",
            isUnlocked = true
        )
        
        val equations = generator.generateUnitsEquations(exerciseType, 3)
        
        assertEquals(3, equations.size)
        equations.forEach { equation ->
            assertEquals(Operation.UNITS_TIME, equation.operation)
        }
    }
    
    @Test
    fun testMixedOperations_PlusMinus() {
        val equation = generator.generateEquation(Operation.PLUS_MINUS, 10)
        
        assertNotNull(equation)
        assertTrue(equation.operation == Operation.PLUS || equation.operation == Operation.MINUS)
        
        when (equation.operation) {
            Operation.PLUS -> assertEquals(equation.componentA + equation.componentB, equation.correctAnswer)
            Operation.MINUS -> assertEquals(equation.componentA - equation.componentB, equation.correctAnswer)
            else -> throw AssertionError("Unexpected operation: ${equation.operation}")
        }
    }
    
    @Test
    fun testNegativeOperations_NegativePlus() {
        val equation = generator.generateEquation(Operation.NEGATIVE_PLUS, 10)
        
        assertNotNull(equation)
        assertEquals(Operation.NEGATIVE_PLUS, equation.operation)
        assertEquals(equation.componentA + equation.componentB, equation.correctAnswer)
        assertTrue(equation.componentA < 0 || equation.componentB < 0) // At least one negative
    }
} 