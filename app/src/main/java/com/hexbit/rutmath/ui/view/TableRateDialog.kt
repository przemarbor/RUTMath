package com.hexbit.rutmath.ui.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import com.hexbit.rutmath.databinding.TableRateDialogBinding
import com.hexbit.rutmath.util.visible

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
        val binding = TableRateDialogBinding.inflate(LayoutInflater.from(context))

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        val stars = arrayOf(binding.star1, binding.star2, binding.star3, binding.star4, binding.star5)
        for (index in 0 until resultPercentage/20) {
            stars[index].visible()
        }
        binding.scoreText.text = " $resultPercentage%"
        binding.okButton.setOnClickListener {
            dismiss()
        }
    }
}