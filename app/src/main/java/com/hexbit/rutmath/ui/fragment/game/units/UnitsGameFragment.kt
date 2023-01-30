package com.hexbit.rutmath.ui.fragment.game.units

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
import com.hexbit.rutmath.ui.view.UnitsHelpDialog
import com.hexbit.rutmath.util.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_units_game.*
import org.koin.android.ext.android.inject
import java.lang.Exception

class UnitsGameFragment : BaseFragment() {

    companion object {
        private const val DEFAULT_INPUT_VALUE = "?"
    }

    override val layout: Int = R.layout.fragment_units_game

    private val args: UnitsGameFragmentArgs by navArgs()

    private val viewModel: UnitsGameViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(
                UnitsGameFragmentDirections.actionUnitsGameFragmentToUnitsListFragment(
                    0,
                    null,
                    args.player
                )
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initKeyboardListener()
        initHelpListener()
        progressBar.max = UnitsGameViewModel.EXERCISES_COUNT
        progressBar.progress = 0
        initViewModel()
    }

    private fun initViewModel() {
        //Load units names from Strings to UnitsGameViewModel
        val timeUnits = getString(R.string.time_units).split(",").toList()
        if (timeUnits.size >= UnitsGameViewModel.UNITS_TIME.size)
            UnitsGameViewModel.UNITS_TIME = timeUnits.subList(0,UnitsGameViewModel.UNITS_TIME.size)


        viewModel.getActiveEquation().observe(viewLifecycleOwner, Observer {
            resetColors()
            equation_p1.text = it.componentA.toString()
                .plus(" ")
                .plus(when (it.operation) {
                    Operation.UNITS_TIME -> UnitsGameViewModel.UNITS_TIME[it.componentAUnitId]
                    Operation.UNITS_LENGTH -> UnitsGameViewModel.UNITS_LENGTH[it.componentAUnitId]
                    Operation.UNITS_WEIGHT -> UnitsGameViewModel.UNITS_WEIGHT[it.componentAUnitId]
                    Operation.UNITS_SURFACE -> UnitsGameViewModel.UNITS_SURFACE[it.componentAUnitId]
                    else -> throw Exception("Invalid operation!")
                })
                .plus(" = ")
            equation_p2.text = when (it.operation) {
                Operation.UNITS_TIME -> UnitsGameViewModel.UNITS_TIME[it.answerUnitId]
                Operation.UNITS_LENGTH -> UnitsGameViewModel.UNITS_LENGTH[it.answerUnitId]
                Operation.UNITS_WEIGHT -> UnitsGameViewModel.UNITS_WEIGHT[it.answerUnitId]
                Operation.UNITS_SURFACE -> UnitsGameViewModel.UNITS_SURFACE[it.answerUnitId]
                else -> throw Exception("Invalid operation!")
            }
            input.text = DEFAULT_INPUT_VALUE
        })

        viewModel.getEndGameEvent().observe(viewLifecycleOwner, Observer { rate ->
            findNavController().navigate(
                UnitsGameFragmentDirections.actionUnitsGameFragmentToUnitsListFragment(
                    rate,
                    args.exerciseType,
                    args.player
                )
            )
        })
        viewModel.getAnswerEvent().observe(viewLifecycleOwner, Observer {
            when (it) {
                UnitsGameViewModel.AnswerEvent.VALID -> {
                    updateUiOnCorrectAnswer()
                    Handler(Looper.getMainLooper()).postDelayed({
                        if(isVisible) {
                            progressBar.progress = progressBar.progress + 1
                            viewModel.setNextActiveEquation()
                        }
                    }, 1000)
                }
                UnitsGameViewModel.AnswerEvent.INVALID -> {
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
                if (input.text.length > 4) {
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

    private fun initHelpListener(){
        helpButton.setOnClickListener{
            viewModel.getActiveEquation().value?.operation?.let { it1 ->
                UnitsHelpDialog(
                    context!!,
                    viewModel.createHelpText(it1)
                ).show()
            }
        }
    }
    private fun updateUiOnErrorAnswer() {
        input.setTextColor(ContextCompat.getColor(context!!, R.color.red))
        equation_p1.setTextColor(ContextCompat.getColor(context!!, R.color.red))
        equation_p2.setTextColor(ContextCompat.getColor(context!!, R.color.red))
    }

    private fun updateUiOnCorrectAnswer() {
        input.setTextColor(ContextCompat.getColor(context!!, R.color.green))
        equation_p1.setTextColor(ContextCompat.getColor(context!!, R.color.green))
        equation_p2.setTextColor(ContextCompat.getColor(context!!, R.color.green))
    }

    private fun resetColors() {
        input.setTextColor(ContextCompat.getColor(context!!, R.color.accent))
        equation_p1.setTextColor(ContextCompat.getColor(context!!, R.color.accent))
        equation_p2.setTextColor(ContextCompat.getColor(context!!, R.color.accent))
    }


}