package com.hexbit.rutmath.shared.di

import com.hexbit.rutmath.shared.data.AndroidDatabaseRepository
import com.hexbit.rutmath.shared.data.DatabaseRepository
import com.hexbit.rutmath.shared.localization.AndroidStringProvider
import com.hexbit.rutmath.shared.localization.StringProvider
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