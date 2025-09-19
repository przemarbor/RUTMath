package com.octbit.rutmath.shared.di

import com.octbit.rutmath.shared.data.AndroidDatabaseRepository
import com.octbit.rutmath.shared.data.DatabaseRepository
import com.octbit.rutmath.shared.localization.AndroidStringProvider
import com.octbit.rutmath.shared.localization.StringProvider
import org.koin.dsl.module

/**
 * Android-specific shared module containing Android implementations.
 */
val androidSharedModule = module {
    
    // Database Repository - Android implementation using Room
    single<DatabaseRepository> { AndroidDatabaseRepository(get()) }
    
    // String Provider - Android implementation using resources
    single<StringProvider> { AndroidStringProvider(get()) }
    
} 