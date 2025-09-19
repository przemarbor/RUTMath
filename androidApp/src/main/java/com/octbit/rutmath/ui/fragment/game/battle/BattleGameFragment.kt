package com.octbit.rutmath.ui.fragment.game.battle

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.octbit.rutmath.R
import com.octbit.rutmath.data.model.Operation
import com.octbit.rutmath.databinding.FragmentBattleGameBinding
import com.octbit.rutmath.ui.view.PlayerBattlePanel
import com.octbit.rutmath.util.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class BattleGameFragment : BaseFragment() {

    companion object {
        private const val POINTS_TO_ACHIEVE = 20
    }

    override val layout: Int = R.layout.fragment_battle_game
    private var _binding: FragmentBattleGameBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BattleFragmentViewModel by viewModel()

    private val player1Panel: PlayerBattlePanel by lazy {
        binding.player1
    }

    private val player2Panel: PlayerBattlePanel by lazy {
        binding.player2
    }
    private val args: BattleGameFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBattleGameBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding){
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAnswersDrawedEvent().observe(viewLifecycleOwner, Observer {
            player1Panel.setAnswers(it)
            player2Panel.setAnswers(it)
        })

        viewModel.getOnNextEquationActiveEvent().observe(viewLifecycleOwner, Observer {
            val equationString = it.let {
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
                    .plus(" = ?")
            }
            player1.setEquation(equationString)
            player2.setEquation(equationString)
        })

        viewModel.getSaveScoreFinishedEvent().observe(viewLifecycleOwner, Observer {
            findNavController().navigate(
                BattleGameFragmentDirections.actionBattleGameFragmentToMenuFragment()
            )
        })

        player1Panel.setListener(object : PlayerBattlePanel.PanelListener {
            override fun onAnswerSelected(value: Int) {
                if (viewModel.isAnswerCorrect(value)) {
                    player1.onCorrectAnswerSelected()
                    player2.clearBonus()
                    if (isGameEnd()) {
                        onGameEnded()
                        return
                    }
                    Handler(Looper.getMainLooper()).postDelayed({
                        viewModel.loadNextEquation()
                    }, 500)
                    return
                } else {
                    player1.onIncorrectAnswerSelected()
                    loadNextEquationIfNeeded()
                }
            }
        })
        player2Panel.setListener(object : PlayerBattlePanel.PanelListener {
            override fun onAnswerSelected(value: Int) {
                if (viewModel.isAnswerCorrect(value)) {
                    player2.onCorrectAnswerSelected()
                    player1.clearBonus()
                    if (isGameEnd()) {
                        onGameEnded()
                        return
                    }
                    Handler(Looper.getMainLooper()).postDelayed({
                        viewModel.loadNextEquation()
                    }, 500)
                    return
                } else {
                    player2.onIncorrectAnswerSelected()
                    loadNextEquationIfNeeded()
                }
            }
        })

        player1Panel.setProgressBar(progress= 0, max= POINTS_TO_ACHIEVE)
        player2Panel.setProgressBar(progress= 0, max= POINTS_TO_ACHIEVE)
    }

    private fun onGameEnded() {
        val alert: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        alert.setTitle(getString(R.string.battle_game_ended))
        alert.setMessage(
            getString(
                R.string.duel_score,
                args.player1nick,
                player1Panel.getResult(),
                args.player2nick,
                player2Panel.getResult()
            )
        )
        alert.setPositiveButton(
            R.string.ok
        ) { _, _ ->
            viewModel.saveScoreInDatabase(
                args.player1nick,
                player1Panel.getResult(),
                args.player2nick,
                player2Panel.getResult()
            )
        }
        alert.show()
    }

    private fun isGameEnd(): Boolean {
        return player1Panel.getProgressBar().progress >= POINTS_TO_ACHIEVE ||
                player2Panel.getProgressBar().progress >= POINTS_TO_ACHIEVE
    }

    private fun loadNextEquationIfNeeded() {
        if (!player1Panel.canAnswer && !player2Panel.canAnswer) {
            Handler(Looper.getMainLooper()).postDelayed({
                viewModel.loadNextEquation()
            }, 500)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
