package com.hexbit.rutmath.ui.fragment.settings

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.hexbit.rutmath.R
import com.hexbit.rutmath.data.AppDatabase
import com.hexbit.rutmath.util.base.BaseFragment
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import org.koin.android.ext.android.inject
import java.util.*


class SettingsFragment : BaseFragment() {

    companion object {
        private const val MAX_NUMBER = 200
        private const val MIN_NUMBER = 5
    }

    override val layout: Int = R.layout.fragment_settings

    private val database: AppDatabase by inject()
    private val disposables = CompositeDisposable()
    private var checkedLanguage = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposables.add(
            database.settingsDao().getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map { it[0] }
                .subscribe { settings ->
                    maxBattleModeNumber.setText(settings.maxNumberInBattleMode.toString())
                    changeLanguageChecked(settings.language)
                }
        )

        rg_language.setOnCheckedChangeListener{ buttonView, isChecked ->
            when(isChecked){
                buttonView.rb_English.id -> {
                    setPreviewText("en")
                }
                buttonView.rb_Polish.id -> {
                    setPreviewText("pl")
                }
                else -> error("error while checking")
            }
        }

        save.setOnClickListener {
            val number = maxBattleModeNumber.text.toString().toInt()
            if (number > MAX_NUMBER) {
                maxBattleModeNumberLayout.error = getString(R.string.settings_fragment_max_number, MAX_NUMBER)
                return@setOnClickListener
            }
            if (number < MIN_NUMBER) {
                maxBattleModeNumberLayout.error = getString(R.string.settings_fragment_min_number, MIN_NUMBER)
                return@setOnClickListener
            }
            maxBattleModeNumberLayout.error = null

            disposables.add(
                database.settingsDao().getAll()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .flatMapCompletable {
                        setLocale(checkedLanguage)
                        it[0].maxNumberInBattleMode = number
                        it[0].language = checkedLanguage
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

    fun setLocale(language:String){
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.setLocale(Locale(language))
        resources.updateConfiguration(conf,dm)
    }

    private fun setPreviewText(language:String){
        checkedLanguage = language

        var conf = resources.configuration
        conf = Configuration(conf)
        conf.setLocale(Locale(language))
        val localizedContext = context!!.createConfigurationContext(conf)
        val resources = localizedContext.resources
        selectLanguageTitle.text = resources.getString(R.string.fragment_settings_select_language)
        maxBattleModeNumberTitle.text = resources.getString(R.string.fragment_settings_max_number)
        save.text = resources.getString(R.string.save)
    }

    private fun changeLanguageChecked(language:String){
        when(language){
            "en" -> rb_English.isChecked = true
            "pl" -> rb_Polish.isChecked = true
            else -> rb_English.isChecked = true
        }
    }
}