package com.hexbit.rutmath.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.hexbit.rutmath.R
import com.hexbit.rutmath.util.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_menu.*

class MenuFragment : BaseFragment() {
    override val layout: Int = R.layout.fragment_menu

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
    }

    private fun initButtons() {
        modesButton.setOnClickListener {
            findNavController().navigate(
                MenuFragmentDirections.actionMenuFragmentToChoosePlayerFragment()
            )
        }
        pvpButton.setOnClickListener {
            findNavController().navigate(
                MenuFragmentDirections.actionMenuFragmentToPlayersNamesFragment()
            )
        }
        settingsButton.setOnClickListener {
            findNavController().navigate(
                MenuFragmentDirections.actionMenuFragmentToSettingsFragment()
            )
        }
        leaderboardButton.setOnClickListener {
            findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToScoreboardFragment())
        }
    }
}
