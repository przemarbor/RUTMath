package com.hexbit.rutmath.di

import androidx.room.Room
import com.hexbit.rutmath.data.AppDatabase
import com.hexbit.rutmath.ui.fragment.addSubList.AddSubListViewModel
import com.hexbit.rutmath.ui.fragment.game.battle.BattleFragmentViewModel
import com.hexbit.rutmath.ui.fragment.choosePlayer.ChoosePlayerViewModel
import com.hexbit.rutmath.ui.fragment.divisibilityList.DivisibilityListViewModel
import com.hexbit.rutmath.ui.fragment.game.divisibility.DivisibilityGameViewModel
import com.hexbit.rutmath.ui.fragment.game.normal.NormalGameViewModel
import com.hexbit.rutmath.ui.fragment.game.table.TableGameViewModel
import com.hexbit.rutmath.ui.fragment.game.units.UnitsGameViewModel
import com.hexbit.rutmath.ui.fragment.mulDivList.MulDivListViewModel
import com.hexbit.rutmath.ui.fragment.unitsList.UnitsListViewModel
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

    viewModel { AddSubListViewModel(get()) }
    viewModel { MulDivListViewModel(get()) }
    viewModel { DivisibilityListViewModel(get()) }
    viewModel { UnitsListViewModel(get()) }
    viewModel { NormalGameViewModel() }
    viewModel { DivisibilityGameViewModel() }
    viewModel { TableGameViewModel() }
    viewModel { UnitsGameViewModel() }
    viewModel { BattleFragmentViewModel(get()) }
    viewModel {
        ChoosePlayerViewModel(
            get()
        )
    }
}