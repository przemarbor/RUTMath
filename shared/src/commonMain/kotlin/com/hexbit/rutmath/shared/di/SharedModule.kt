package com.hexbit.rutmath.shared.di

import com.hexbit.rutmath.shared.game.EquationGenerator
import com.hexbit.rutmath.shared.game.MathEquationGenerator
import com.hexbit.rutmath.shared.usecase.GameUseCase
import com.hexbit.rutmath.shared.usecase.UnitsGameUseCase
import com.hexbit.rutmath.shared.usecase.DataUseCase
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