package com.octbit.rutmath.ui.fragment.choosePlayer

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.octbit.rutmath.R
import com.octbit.rutmath.databinding.FragmentChoosePlayerBinding
import com.octbit.rutmath.util.base.BaseFragment
import com.octbit.rutmath.util.gone
import com.octbit.rutmath.util.visible
import org.koin.androidx.viewmodel.ext.android.viewModel


class ChoosePlayerFragment : BaseFragment() {

    override val layout: Int = R.layout.fragment_choose_player
    private var _binding: FragmentChoosePlayerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChoosePlayerViewModel by viewModel()

    private val choosePlayerAdapter: ChoosePlayerAdapter by lazy {
        ChoosePlayerAdapter { player ->
            findNavController().navigate(
                ChoosePlayerFragmentDirections.actionChoosePlayerFragmentToChooseModeFragment(
                    player = player,
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
        _binding = FragmentChoosePlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initView()
    }

    private fun initView() = with(binding) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = choosePlayerAdapter
        }
        addPlayer.setOnClickListener {
            showAddPlayerDialog()
        }
    }

    private fun initViewModel() = with(binding) {
        viewModel.getRefreshPlayersListEvent().observeForever(Observer {
            viewModel.getPlayersList().let { playersList ->
                if (playersList.isEmpty()) {
                    emptyListInfo.visible()
                } else {
                    emptyListInfo.gone()
                    choosePlayerAdapter.refreshAdapter(playersList)
                }
            }

        })
        viewModel.playerCreationEvent().observeForever(Observer { creationEvent ->
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
        showSimpleDialog(getString(R.string.error), getString(R.string.choose_player_nick_exist))
    }

    private fun showNicknameIsEmptyDialog() {
        showSimpleDialog(getString(R.string.error), getString(R.string.nick_empty))
    }

    private fun showAddPlayerDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.choose_player_input))
        val input = EditText(requireContext())
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}