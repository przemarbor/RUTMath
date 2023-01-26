package com.hexbit.rutmath.ui.activity

import androidx.appcompat.app.AppCompatActivity
import com.hexbit.rutmath.R
import com.hexbit.rutmath.data.AppDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import java.util.*

class MainActivity : AppCompatActivity() {
    private val database: AppDatabase by inject()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        //Set app language from database and then set view to MainActivity
        database.settingsDao().getAll()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map { cos ->
                setLocale(cos[0].language)
                setContentView(R.layout.main_activity)
            }
            .subscribe()
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
