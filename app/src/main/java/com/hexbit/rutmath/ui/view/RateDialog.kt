package com.hexbit.rutmath.ui.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.hexbit.rutmath.R
import com.hexbit.rutmath.util.visible
import kotlinx.android.synthetic.main.rate_dialog.*

/**
 * Dialog with stars after finish NormalGame
 *
 * #// Pop-up wyświetlający ilość zdobytych gwiazdek w zadaniu.
 */
class RateDialog(context: Context, private val rate: Int) : Dialog(context) {

    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.rate_dialog)

        val stars = arrayOf(star1, star2, star3, star4, star5)
        for (index in 0 until rate) {
            stars[index].visible()
        }
        okButton.setOnClickListener {
            dismiss()
        }
    }
}