package com.octbit.rutmath.ui.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import com.octbit.rutmath.databinding.UnitsHelpDialogBinding


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
        val binding = UnitsHelpDialogBinding.inflate(LayoutInflater.from(context))
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        binding.helpText.text = text
        binding.okButton.setOnClickListener {
            dismiss()
        }
    }
}