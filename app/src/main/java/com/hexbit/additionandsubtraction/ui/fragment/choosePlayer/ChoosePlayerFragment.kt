package com.hexbit.additionandsubtraction.ui.fragment.choosePlayer

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hexbit.additionandsubtraction.R
import com.hexbit.additionandsubtraction.util.base.BaseFragment
import com.hexbit.additionandsubtraction.util.gone
import com.hexbit.additionandsubtraction.util.visible
import kotlinx.android.synthetic.main.choose_player_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ChoosePlayerFragment : BaseFragment() {

    override val layout: Int = R.layout.choose_player_fragment

    private val viewModel: ChoosePlayerViewModel by viewModel()

    private val choosePlayerAdapter: ChoosePlayerAdapter by lazy {
        ChoosePlayerAdapter { player ->
            findNavController().navigate(
                ChoosePlayerFragmentDirections.actionChoosePlayerFragmentToExerciseListFragment(
                    rate = -1,
                    exerciseType = null,
                    player = player
                )
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initView()
    }

    private fun initView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = choosePlayerAdapter
        }
        addPlayer.setOnClickListener {
            showAddPlayerDialog()
        }
    }

    private fun initViewModel() {
        viewModel.getRefreshPlayersListEvent().observe(this, Observer {
            viewModel.getPlayersList().let { playersList ->
                if (playersList.isEmpty()) {
                    emptyListInfo.visible()
                } else {
                    emptyListInfo.gone()
                    choosePlayerAdapter.refreshAdapter(playersList)
                }
            }

        })
        viewModel.playerCreationEvent().observe(this, Observer { creationEvent ->
            creationEvent?.let {
                when (it) {
                    ChoosePlayerViewModel.PlayerCreationEvent.SUCCESS -> viewModel.loadPlayersList()
                    ChoosePlayerViewModel.PlayerCreationEvent.NICKNAME_EXISTS -> showNicknameExistsDialog()
                    ChoosePlayerViewModel.PlayerCreationEvent.NICKNAME_EMPTY -> showNicknameIsEmptyDialog()
                }
            }
        })
        viewModel.loadPlayersList()
    }

    private fun showNicknameExistsDialog() {
        showSimpleDialog(getString(R.string.error), getString(R.string.nickname_exists))
    }

    private fun showNicknameIsEmptyDialog() {
        showSimpleDialog(getString(R.string.error), getString(R.string.nickname_empty))
    }

    private fun showAddPlayerDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context!!)
        builder.setTitle(getString(R.string.input_nickname))
        val input = EditText(context!!)
        builder.setView(input)
        builder.setPositiveButton(
            getString(R.string.ok)
        ) { dialog, _ ->
            viewModel.createPlayer(input.text.toString())
            dialog.dismiss()
        }
        builder.setNegativeButton(
            getString(R.string.cancel)
        ) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}