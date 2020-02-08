package com.hexbit.additionandsubtraction.ui.fragment.settings

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.hexbit.additionandsubtraction.R
import com.hexbit.additionandsubtraction.data.AppDatabase
import com.hexbit.additionandsubtraction.util.base.BaseFragment
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.android.ext.android.inject

class SettingsFragment : BaseFragment() {

    companion object {
        private const val MAX_NUMBER = 100
        private const val MIN_NUMBER = 5
    }

    override val layout: Int = R.layout.fragment_settings

    private val database: AppDatabase by inject()

    private val disposables = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposables.add(
            database.settingsDao().getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map { it[0].maxNumberInBattleMode }
                .subscribe { numberFromDatabase ->
                    maxBattleModeNumber.setText(numberFromDatabase.toString())
                }
        )

        save.setOnClickListener {
            val number = maxBattleModeNumber.text.toString().toInt()
            if (number > MAX_NUMBER) {
                maxBattleModeNumberLayout.error = getString(R.string.max_number_reached, MAX_NUMBER)
                return@setOnClickListener
            }
            if (number < MIN_NUMBER) {
                maxBattleModeNumberLayout.error = getString(R.string.min_number_reached, MIN_NUMBER)
                return@setOnClickListener
            }
            maxBattleModeNumberLayout.error = null

            disposables.add(
                database.settingsDao().getAll()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .flatMapCompletable {
                        it[0].maxNumberInBattleMode = number
                        database.settingsDao().update(it[0])
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                    }
                    .andThen(Completable.defer {
                        findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToMenuFragment())
                        Completable.complete()
                    })
                    .subscribe()
            )
        }
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}