package com.hexbit.additionandsubtraction.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.hexbit.additionandsubtraction.R
import com.hexbit.additionandsubtraction.util.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_menu.*

class MenuFragment : BaseFragment() {
    override val layout: Int = R.layout.fragment_menu

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
    }

    private fun initButtons() {
        normalModeButton.setOnClickListener {
            findNavController().navigate(
                MenuFragmentDirections.actionMenuFragmentToChoosePlayerFragment()
            )
        }
        battleModeButton.setOnClickListener {
            findNavController().navigate(
                MenuFragmentDirections.actionMenuFragmentToPlayersNamesFragment()
            )
        }
        settingsButton.setOnClickListener {
            findNavController().navigate(
                MenuFragmentDirections.actionMenuFragmentToSettingsFragment()
            )
        }
        scoreboardButton.setOnClickListener {
            findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToScoreboardFragment())
        }
    }
}
