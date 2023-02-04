package com.hexbit.rutmath.ui.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.hexbit.rutmath.R
import kotlinx.android.synthetic.main.units_help_dialog.*


/**
 * Dialog after clicking help in UnitsGame
 *
 */
class UnitsHelpDialog(context: Context, private val text: String) : Dialog(context) {

    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.units_help_dialog)
        helpText.text = text
        okButton.setOnClickListener {
            dismiss()
        }
    }
}