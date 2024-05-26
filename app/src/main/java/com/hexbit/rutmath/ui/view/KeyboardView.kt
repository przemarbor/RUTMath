package com.hexbit.rutmath.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.hexbit.rutmath.R
import com.hexbit.rutmath.databinding.KeyboardBinding

class KeyboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    interface InputListener {
        fun onNumberClicked(value: Int)

        fun onBackspaceClicked()

        fun onAcceptClicked()
    }

    private var inputListener: InputListener? = null

    private var _binding: KeyboardBinding? = null
    private val binding get() = _binding!!

    init {
        _binding = KeyboardBinding.inflate(LayoutInflater.from(context), this, true)
        LayoutInflater.from(context)
            .inflate(R.layout.keyboard, this, true)
        initListeners()
    }

    private fun initListeners() = with(binding){
        button0.setOnClickListener { inputListener?.onNumberClicked(0) }
        button1.setOnClickListener { inputListener?.onNumberClicked(1) }
        button2.setOnClickListener { inputListener?.onNumberClicked(2) }
        button3.setOnClickListener { inputListener?.onNumberClicked(3) }
        button4.setOnClickListener { inputListener?.onNumberClicked(4) }
        button5.setOnClickListener { inputListener?.onNumberClicked(5) }
        button6.setOnClickListener { inputListener?.onNumberClicked(6) }
        button7.setOnClickListener { inputListener?.onNumberClicked(7) }
        button8.setOnClickListener { inputListener?.onNumberClicked(8) }
        button9.setOnClickListener { inputListener?.onNumberClicked(9) }
        backspace.setOnClickListener { inputListener?.onBackspaceClicked() }
        accept.setOnClickListener { inputListener?.onAcceptClicked() }
    }

    fun disableKeyboard() = with(binding){
        button0.isClickable = false
        button1.isClickable = false
        button2.isClickable = false
        button3.isClickable = false
        button4.isClickable = false
        button5.isClickable = false
        button6.isClickable = false
        button7.isClickable = false
        button8.isClickable = false
        button9.isClickable = false
        backspace.isClickable = false
        accept.isClickable = false
    }


    fun enableKeyboard() = with(binding){
        button0.isClickable = true
        button1.isClickable = true
        button2.isClickable = true
        button3.isClickable = true
        button4.isClickable = true
        button5.isClickable = true
        button6.isClickable = true
        button7.isClickable = true
        button8.isClickable = true
        button9.isClickable = true
        backspace.isClickable = true
        accept.isClickable = true
    }


    fun setListener(inputListener: InputListener) {
        this.inputListener = inputListener
    }
}