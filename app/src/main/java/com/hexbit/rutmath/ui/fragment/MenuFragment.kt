package com.hexbit.rutmath.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.hexbit.rutmath.R
import com.hexbit.rutmath.databinding.FragmentMenuBinding
import com.hexbit.rutmath.util.base.BaseFragment

class MenuFragment : BaseFragment() {
    override val layout: Int = R.layout.fragment_menu
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
    }

    private fun initButtons() = with(binding) {
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
