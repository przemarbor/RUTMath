package com.hexbit.rutmath.ui.fragment.game.table

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hexbit.rutmath.R
import com.hexbit.rutmath.data.model.Operation
import com.hexbit.rutmath.ui.view.KeyboardView
import com.hexbit.rutmath.util.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_table_game.*
import org.koin.android.ext.android.inject
import java.lang.Exception

class TableGameFragment : BaseFragment() {

    companion object {
        private const val DEFAULT_INPUT_VALUE = "?"
    }

    override val layout: Int = R.layout.fragment_table_game

    private val args: TableGameFragmentArgs by navArgs()

    private val viewModel: TableGameViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(
                TableGameFragmentDirections.actionTableGameFragmentToChooseModeFragment(
                    args.player,
                    res = -1
                )
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initKeyboardListener()
        progressBar.max = TableGameViewModel.EXERCISES_COUNT
        progressBar.progress = 0
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.getActiveEquation().observe(viewLifecycleOwner, Observer {
            resetColors()
            equation.text = it.let {
                it.componentA.toString()
                    .plus(" ")
                    .plus(
                        when (it.operation) {
                            Operation.PLUS -> "+"
                            Operation.MINUS -> "-"
                            Operation.MULTIPLY -> "×"
                            Operation.DIVIDE -> "÷"
                            else -> throw Exception("Invalid operation!")
                        }
                    )
                    .plus(" ")
                    .plus(it.componentB)
                    .plus(" = ")
            }
            input.text = DEFAULT_INPUT_VALUE
        })

        viewModel.getEndGameEvent().observe(viewLifecycleOwner, Observer { rate ->
            findNavController().navigate(
                TableGameFragmentDirections.actionTableGameFragmentToChooseModeFragment(
                    args.player,
                    rate
                )
            )
        })
        viewModel.getAnswerEvent().observe(viewLifecycleOwner, Observer {
            when (it) {
                TableGameViewModel.AnswerEvent.VALID -> {
                    updateUiOnCorrectAnswer()
                    Handler(Looper.getMainLooper()).postDelayed({
                        if(isVisible) {
                            progressBar.progress = progressBar.progress + 1
                            viewModel.setNextActiveEquation()
                        }
                    }, 1000)
                }
                TableGameViewModel.AnswerEvent.INVALID -> {
                    updateUiOnErrorAnswer()
                    viewModel.markActiveEquationAsFailed()
                }
                null -> throw Exception("Error: AnswerEvent is null")
            }
        })
        viewModel.init(args)
    }

    private fun initKeyboardListener() {
        keyboardView.setListener(object : KeyboardView.InputListener {
            @SuppressLint("SetTextI18n")
            override fun onNumberClicked(value: Int) {
                if (input.text.length > 2) {
                    return
                }
                if (input.text.toString() == DEFAULT_INPUT_VALUE) {
                    input.text = ""
                }
                input.text = input.text.toString() + value
                resetColors()
            }

            override fun onBackspaceClicked() {
                resetColors()
                if (input.text.length <= 1) {
                    input.text = DEFAULT_INPUT_VALUE
                } else {
                    input.text =
                        input.text.toString().substring(0, input.text.toString().length - 1)
                }
            }

            override fun onAcceptClicked() {
                try {
                    val userAnswer = input.text.toString().toInt()
                    viewModel.validateAnswer(userAnswer)
                } catch (exception: Exception) {
                    return
                }
            }

        })
    }

    private fun updateUiOnErrorAnswer() {
        input.setTextColor(ContextCompat.getColor(context!!, R.color.red))
        equation.setTextColor(ContextCompat.getColor(context!!, R.color.red))
    }

    private fun updateUiOnCorrectAnswer() {
        input.setTextColor(ContextCompat.getColor(context!!, R.color.green))
        equation.setTextColor(ContextCompat.getColor(context!!, R.color.green))
    }

    private fun resetColors() {
        input.setTextColor(ContextCompat.getColor(context!!, R.color.accent))
        equation.setTextColor(ContextCompat.getColor(context!!, R.color.accent))
    }

}