package com.hexbit.rutmath.ui.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import com.hexbit.rutmath.databinding.NormalRateDialogBinding
import com.hexbit.rutmath.util.visible

/**
 * Dialog with stars after finishing NormalGame
 *
 */
class NormalRateDialog(context: Context, private val rate: Int) : Dialog(context) {

    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = NormalRateDialogBinding.inflate(LayoutInflater.from(context))
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        val stars = arrayOf(binding.star1, binding.star2, binding.star3, binding.star4, binding.star5)
        for (index in 0 until rate) {
            stars[index].visible()
        }
        binding.okButton.setOnClickListener {
            dismiss()
        }
    }
}