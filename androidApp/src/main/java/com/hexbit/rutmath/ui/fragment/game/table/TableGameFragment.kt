package com.octbit.rutmath.ui.fragment.game.table

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.octbit.rutmath.R
import com.octbit.rutmath.data.model.Operation
import com.octbit.rutmath.databinding.FragmentTableGameBinding
import com.octbit.rutmath.ui.fragment.game.normal.NormalGameFragment
import com.octbit.rutmath.ui.view.KeyboardView
import com.octbit.rutmath.util.base.BaseFragment
import org.koin.android.ext.android.inject
import java.lang.Exception

class TableGameFragment : BaseFragment() {

    companion object {
        private const val DEFAULT_INPUT_VALUE = "?"
    }

    override val layout: Int = R.layout.fragment_table_game
    private var _binding: FragmentTableGameBinding? = null
    private val binding get() = _binding!!
    private val args: TableGameFragmentArgs by navArgs()

    private val viewModel: TableGameViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(
                TableGameFragmentDirections.actionTableGameFragmentToChooseModeFragment(
                    player = args.player,
                    res = -1
                )
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTableGameBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        initKeyboardListener()
        progressBar.max = TableGameViewModel.EXERCISES_COUNT
        progressBar.progress = 0
        initViewModel()
    }

    private fun initViewModel() = with(binding) {
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
                    player = args.player,
                    res = rate
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
                            keyboardView.enableKeyboard()
                        }
                    }, 1000)
                }
                TableGameViewModel.AnswerEvent.INVALID -> {
                    updateUiOnErrorAnswer()
                    viewModel.markActiveEquationAsFailed()
                    keyboardView.enableKeyboard()
                }
                null -> throw Exception("Error: AnswerEvent is null")
            }
        })
        viewModel.init(args)
    }

    private fun initKeyboardListener() = with(binding) {
        keyboardView.setListener(object : KeyboardView.InputListener {
            @SuppressLint("SetTextI18n")
            override fun onNumberClicked(value: Int) {
                if (input.text.length > 2) {
                    return
                }
                var p = input.text.toString();
                if (p == DEFAULT_INPUT_VALUE) {
                    p = ""
                } else if (p == "-$DEFAULT_INPUT_VALUE")
                {
                    p = "-"
                }
                input.text = p + value
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
                    if (input.text != DEFAULT_INPUT_VALUE){
                        updateUiOnErrorAnswer()
                        keyboardView.disableKeyboard()
                        val userAnswer = input.text.toString().toInt()
                        viewModel.validateAnswer(userAnswer)
                    }

                } catch (exception: Exception) {
                    return
                }
            }

            override fun onNegativeClicked(){
                val s = input.text.toString()
                if (s.startsWith("-", 0)) {
                    input.text = s.substring(1)
                }
                else {
                    val ss = "-" + s
                    input.text = ss
                }
            }

        })
    }

    private fun updateUiOnErrorAnswer() = with(binding) {
        input.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
        equation.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
    }

    private fun updateUiOnCorrectAnswer() = with(binding) {
        input.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        equation.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
    }

    private fun resetColors() = with(binding){
        input.setTextColor(ContextCompat.getColor(requireContext(), R.color.accent))
        equation.setTextColor(ContextCompat.getColor(requireContext(), R.color.accent))
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
