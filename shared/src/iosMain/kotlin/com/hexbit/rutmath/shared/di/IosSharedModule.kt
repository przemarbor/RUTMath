package com.hexbit.rutmath.shared.di

import com.hexbit.rutmath.shared.data.DatabaseRepository
import com.hexbit.rutmath.shared.data.IosDatabaseRepository
import com.hexbit.rutmath.shared.localization.IosStringProvider
import com.hexbit.rutmath.shared.localization.StringProvider
import org.koin.dsl.module

/**
 * iOS-specific shared module containing iOS implementations.
 */
val iosSharedModule = module {
    
    // Database Repository - iOS implementation using UserDefaults
    single<DatabaseRepository> { IosDatabaseRepository() }
    
    // String Provider - iOS implementation using NSLocalizedString
    single<StringProvider> { IosStringProvider() }
    
} 