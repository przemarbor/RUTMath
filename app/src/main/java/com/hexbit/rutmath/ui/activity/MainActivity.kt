package com.hexbit.rutmath.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hexbit.rutmath.R
import com.hexbit.rutmath.data.AppDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import java.util.*

class MainActivity : AppCompatActivity() {
    private val disposables = CompositeDisposable()
    private val database: AppDatabase by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disposables.add(
            database.settingsDao().getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map { it[0].language }
                .subscribe { language ->
                    setLocale(language)
                    setContentView(R.layout.main_activity)
                }
        )
    }
    private fun setLocale(language:String){
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.setLocale(Locale(language))
        resources.updateConfiguration(conf,dm)
    }
}
