package com.hexbit.rutmath.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hexbit.rutmath.R
import com.hexbit.rutmath.data.AppDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import io.reactivex.disposables.CompositeDisposable
import java.util.*

var isRecreated = false
class MainActivity : AppCompatActivity() {
    private val database: AppDatabase by inject()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isRecreated){
            database.settingsDao().getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map { databaseSettings ->
                    if (databaseSettings.isNotEmpty()) {
                        setLocale(databaseSettings[0].language)
                        isRecreated = true
                        recreate()
                    }
                }.subscribe()
        }
        setContentView(R.layout.main_activity)
    }

    /**
     * Sets the application language
     */
    private fun setLocale(language:String){
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.setLocale(Locale(language))
        resources.updateConfiguration(conf,dm)
    }
}
