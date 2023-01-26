package com.hexbit.rutmath.ui.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.hexbit.rutmath.R
import com.hexbit.rutmath.util.visible
import kotlinx.android.synthetic.main.table_rate_dialog.*

/**
 * Dialog with stars after finishing TableGame
 *
 */
class TableRateDialog(context: Context, private val resultPercentage: Int) : Dialog(context) {

    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.table_rate_dialog)

        val stars = arrayOf(star1, star2, star3, star4, star5)
        for (index in 0 until resultPercentage/20) {
            stars[index].visible()
        }
        scoreText.text = " $resultPercentage%"
        okButton.setOnClickListener {
            dismiss()
        }
    }
}