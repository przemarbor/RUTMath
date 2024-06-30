package com.hexbit.rutmath.ui.fragment.settings

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.hexbit.rutmath.R
import com.hexbit.rutmath.data.AppDatabase
import com.hexbit.rutmath.databinding.FragmentSettingsBinding
import com.hexbit.rutmath.util.base.BaseFragment
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import java.util.*

class SettingsFragment() : BaseFragment() {
    override val layout: Int = R.layout.fragment_menu
    companion object {
        private const val MAX_NUMBER = 200
        private const val MIN_NUMBER = 5
    }
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val database: AppDatabase by inject()
    private val disposables = CompositeDisposable()
    private var checkedLanguage = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding){
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

        rgLanguage.setOnCheckedChangeListener{ _ , isChecked ->
            when(isChecked){
                rbEnglish.id -> {
                    setPreviewText("en")
                }
                rbPolish.id -> {
                    setPreviewText("pl")
                }
                rbFrench.id -> {
                    setPreviewText("fr")
                }
                rbPortuguese.id -> {
                    setPreviewText("pt")
                }
                rbGreek.id->{
                    setPreviewText("el")
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
        _binding = null
    }

    fun setLocale(language:String){
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.setLocale(Locale(language))
        resources.updateConfiguration(conf,dm)
    }

    private fun setPreviewText(language:String) = with(binding){
        checkedLanguage = language

        var conf = resources.configuration
        conf = Configuration(conf)
        conf.setLocale(Locale(language))
        val localizedContext = requireContext().createConfigurationContext(conf)
        val resources = localizedContext.resources
        selectLanguageTitle.text = resources.getString(R.string.fragment_settings_select_language)
        maxBattleModeNumberTitle.text = resources.getString(R.string.fragment_settings_max_number)
        save.text = resources.getString(R.string.save)
    }

    private fun changeLanguageChecked(language:String) = with(binding){
        when(language){
            "en" -> rbEnglish.isChecked = true
            "pl" -> rbPolish.isChecked = true
            "fr" -> rbFrench.isChecked = true
            "pt" -> rbPortuguese.isChecked = true
            "el" -> rbGreek.isChecked=true
            else -> rbEnglish.isChecked = true
        }
    }
}
