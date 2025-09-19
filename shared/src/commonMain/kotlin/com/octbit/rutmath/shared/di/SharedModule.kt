package com.octbit.rutmath.shared.di

import com.octbit.rutmath.shared.game.EquationGenerator
import com.octbit.rutmath.shared.game.MathEquationGenerator
import com.octbit.rutmath.shared.usecase.GameUseCase
import com.octbit.rutmath.shared.usecase.UnitsGameUseCase
import com.octbit.rutmath.shared.usecase.DataUseCase
import org.koin.dsl.module

/**
 * Shared Koin module containing platform-independent dependencies.
 */
val sharedModule = module {
    
    // Equation Generation
    single<EquationGenerator> { MathEquationGenerator() }
    
    // Use Cases
    single { GameUseCase(get()) }
    single { UnitsGameUseCase(get()) }
    single { DataUseCase(get()) }
    
} 