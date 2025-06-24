package com.hexbit.rutmath.ui.fragment

import android.content.Intent
import android.net.Uri
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
        // University Logo in top left corner - redirects to PRz Website based on language selected

        przLogo.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.prz_url))))
        }

        // Faculty Logo in top center - redirects to WEiI Website based on language selected
        weiiLogo.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.weii_url))))
        }

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
