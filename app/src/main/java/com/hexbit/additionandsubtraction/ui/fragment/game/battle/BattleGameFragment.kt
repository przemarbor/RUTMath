package com.hexbit.additionandsubtraction.ui.fragment.game.battle

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hexbit.additionandsubtraction.R
import com.hexbit.additionandsubtraction.data.model.Operation
import com.hexbit.additionandsubtraction.ui.view.PlayerBattlePanel
import com.hexbit.additionandsubtraction.util.base.BaseFragment
import kotlinx.android.synthetic.main.battle_player_view.view.*
import kotlinx.android.synthetic.main.fragment_battle_game.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class BattleGameFragment : BaseFragment() {

    companion object {
        private const val POINTS_TO_ACHIEVE = 10
    }

    override val layout: Int = R.layout.fragment_battle_game

    private val viewModel: BattleFragmentViewModel by viewModel()

    private val player1Panel: PlayerBattlePanel by lazy {
        player1
    }

    private val player2Panel: PlayerBattlePanel by lazy {
        player2
    }
    private val args: BattleGameFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
                            Operation.PLUS_MINUS -> throw Exception("Invalid operation!")
                        }
                    )
                    .plus(" ")
                    .plus(it.componentB)
                    .plus(" = ?")
            }
            player1.equation.text = equationString
            player2.equation.text = equationString
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
                    Handler().postDelayed({
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
                    Handler().postDelayed({
                        viewModel.loadNextEquation()
                    }, 500)
                    return
                } else {
                    player2.onIncorrectAnswerSelected()
                    loadNextEquationIfNeeded()
                }
            }
        })

        player1Panel.progressBar.max = POINTS_TO_ACHIEVE
        player2Panel.progressBar.max = POINTS_TO_ACHIEVE
    }

    private fun onGameEnded() {
        val alert: AlertDialog.Builder = AlertDialog.Builder(context!!)
        alert.setTitle(getString(R.string.game_ended))
        alert.setMessage(
            getString(
                R.string.score,
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
        return player1Panel.progressBar.progress >= POINTS_TO_ACHIEVE ||
                player2Panel.progressBar.progress >= POINTS_TO_ACHIEVE
    }

    private fun loadNextEquationIfNeeded() {
        if (!player1Panel.canAnswer && !player2Panel.canAnswer) {
            Handler().postDelayed({
                viewModel.loadNextEquation()
            }, 500)
        }
    }
}