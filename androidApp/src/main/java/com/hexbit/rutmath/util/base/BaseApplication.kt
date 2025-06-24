package com.hexbit.rutmath.util.base

import android.annotation.SuppressLint
import android.app.Application
import com.hexbit.rutmath.R
import com.hexbit.rutmath.data.AppDatabase
import com.hexbit.rutmath.data.model.Settings
import com.hexbit.rutmath.di.appModule
import com.hexbit.rutmath.shared.di.androidSharedModule
import com.hexbit.rutmath.shared.di.sharedModule
import com.hexbit.rutmath.shared.usecase.DataUseCase
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
//import org.koin.android.ext.android.startKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Main application object.
 */
class BaseApplication : Application() {

    private val database: AppDatabase by inject()
    private val dataUseCase: DataUseCase by inject()
    
    // Application scope for coroutines
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BaseApplication)
            modules(
                appModule,           // Existing Android module
                sharedModule,        // Shared KMP module
                androidSharedModule  // Android-specific shared module
            )
        }
        initDatabaseIfNeeded()
        initSharedData()
    }

    @SuppressLint("CheckResult")
    private fun initDatabaseIfNeeded() {
        database.settingsDao().getAll()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .flatMapCompletable { databaseSettings ->
                if (databaseSettings.isEmpty()) {
                    database.settingsDao()
                        .insertAll(
                            arrayListOf(
                                Settings(
                                    maxNumberInBattleMode = 100,
                                    lastNickname1 = getString(R.string.player1),
                                    lastNickname2 = getString(R.string.player2),
                                    language = "en"
                                )
                            )
                        )
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                } else {
                    Completable.complete()
                }
            }
            .subscribe()
    }
    
    /**
     * Initialize shared data using the new KMP architecture.
     */
    private fun initSharedData() {
        applicationScope.launch {
            try {
                // Initialize default exercise types if needed
                dataUseCase.initializeDefaultExercisesIfNeeded()
                
                // Ensure settings exist
                dataUseCase.getSettings()
            } catch (e: Exception) {
                // Log error in production
                e.printStackTrace()
            }
        }
    }
}