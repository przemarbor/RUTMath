package com.hexbit.rutmath.di

import androidx.room.Room
import com.hexbit.rutmath.data.AppDatabase
import com.hexbit.rutmath.ui.fragment.exercise.list.ExerciseListViewModel
import com.hexbit.rutmath.ui.fragment.game.battle.BattleFragmentViewModel
import com.hexbit.rutmath.ui.fragment.choosePlayer.ChoosePlayerViewModel
import com.hexbit.rutmath.ui.fragment.game.normal.NormalGameViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            AppDatabase.DATABASE_FILE
        ).build()
    }

    viewModel { ExerciseListViewModel(get()) }
    viewModel { NormalGameViewModel() }
    viewModel { BattleFragmentViewModel(get()) }
    viewModel {
        ChoosePlayerViewModel(
            get()
        )
    }
}