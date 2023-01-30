package com.hexbit.rutmath.ui.fragment.game.divisibility

import android.content.Context
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
import com.hexbit.rutmath.util.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_divisibility_game.*
import org.koin.android.ext.android.inject
import java.lang.Exception


class DivisibilityGameFragment : BaseFragment() {

    override val layout: Int = R.layout.fragment_divisibility_game
    private val args: DivisibilityGameFragmentArgs by navArgs()
    private val viewModel: DivisibilityGameViewModel by inject()
    private var question = listOf<String>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        question = listOf(
            resources.getString(R.string.divisibility_question_p1),
            resources.getString(R.string.divisibility_question_p2)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(
                DivisibilityGameFragmentDirections.actionDivisibilityGameFragmentToDivisibilityListFragment(
                    0,
                    null,
                    args.player
                )
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAnswerButtons()
        progressBar.max = DivisibilityGameViewModel.EXERCISES_COUNT
        progressBar.progress = 0
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.getActiveEquation().observe(viewLifecycleOwner, Observer {
            resetColors()
            equationText.text = it.let {
                question[0]
                    .plus(" ")
                    .plus(it.componentA.toString())
                    .plus(" ")
                    .plus(question[1])
                    .plus(" ")
                    .plus(it.componentB.toString())
                    .plus("?")
            }
            equationNumbers.text = it.let {
                it.componentA.toString()
                    .plus(" ÷ ")
                    .plus(it.componentB.toString())
            }
        })

        viewModel.getEndGameEvent().observe(viewLifecycleOwner, Observer { rate ->
            findNavController().navigate(
                DivisibilityGameFragmentDirections.actionDivisibilityGameFragmentToDivisibilityListFragment(
                    rate,
                    args.exerciseType,
                    args.player
                )
            )
        })
        viewModel.getAnswerEvent().observe(viewLifecycleOwner, Observer {
            when (it) {
                DivisibilityGameViewModel.AnswerEvent.VALID -> {
                    updateUiOnCorrectAnswer()
                    Handler(Looper.getMainLooper()).postDelayed({
                        if(isVisible) {
                            progressBar.progress = progressBar.progress + 1
                            viewModel.setNextActiveEquation()
                        }
                    }, 800)
                }
                DivisibilityGameViewModel.AnswerEvent.INVALID -> {
                    updateUiOnErrorAnswer()
                    Handler(Looper.getMainLooper()).postDelayed({
                        if(isVisible) {
                            viewModel.markActiveEquationAsFailed()
                            viewModel.setNextActiveEquation()
                        }
                    }, 2000)
                }
                null -> throw Exception("Error: AnswerEvent is null")
            }
        })
        viewModel.init(args)
    }

    private fun initAnswerButtons() {
        yesButton.setOnClickListener {
            try {
                val userAnswer = 1
                viewModel.validateAnswer(userAnswer)
            } catch (exception: Exception) { }
        }
        noButton.setOnClickListener {
            try {
                val userAnswer = 0
                viewModel.validateAnswer(userAnswer)
            } catch (exception: Exception) { }
        }
    }


    private fun updateUiOnErrorAnswer() {
        equationText.setTextColor(ContextCompat.getColor(context!!, R.color.red))
        equationNumbers.setTextColor(ContextCompat.getColor(context!!, R.color.red))
    }

    private fun updateUiOnCorrectAnswer() {
        equationText.setTextColor(ContextCompat.getColor(context!!, R.color.green))
        equationNumbers.setTextColor(ContextCompat.getColor(context!!, R.color.green))
    }

    private fun resetColors() {
        equationText.setTextColor(ContextCompat.getColor(context!!, R.color.accent))
        equationNumbers.setTextColor(ContextCompat.getColor(context!!, R.color.accent))
    }

}