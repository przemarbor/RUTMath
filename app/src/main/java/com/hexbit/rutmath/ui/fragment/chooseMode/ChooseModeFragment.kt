package com.hexbit.rutmath.ui.fragment.chooseMode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hexbit.rutmath.util.base.BaseFragment
import com.hexbit.rutmath.R
import com.hexbit.rutmath.databinding.FragmentChooseModeBinding
import com.hexbit.rutmath.ui.view.TableRateDialog


class ChooseModeFragment : BaseFragment() {
    override val layout: Int = R.layout.fragment_choose_mode
    private var _binding: FragmentChooseModeBinding? = null
    private val binding get() = _binding!!
    private val args: ChooseModeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChooseModeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.res.let {
            if (args.res > 0){
                TableRateDialog(requireContext(), args.res).show()
            }}
        initButtons()
    }

    private fun initButtons() = with(binding){
        addSubLayout.setOnClickListener {
            findNavController().navigate(
                ChooseModeFragmentDirections.actionChooseModeFragmentToAddSubListFragment(
                    rate = -1,
                    exerciseType = null,
                    player = args.player
                )
            )
        }
        mulDivLayout.setOnClickListener {
            findNavController().navigate(
                ChooseModeFragmentDirections.actionChooseModeFragmentToMulDivListFragment(
                    rate = -1,
                    exerciseType = null,
                    player = args.player
                )
            )
        }
        divisibilityLayout.setOnClickListener {
            findNavController().navigate(
                ChooseModeFragmentDirections.actionChooseModeFragmentToDivisibilityListFragment(
                    rate = -1,
                    exerciseType = null,
                    player = args.player
                )
            )
        }
        unitsLayout.setOnClickListener {
            findNavController().navigate(
                ChooseModeFragmentDirections.actionChooseModeFragmentToUnitsListFragment(
                    rate = -1,
                    exerciseType = null,
                    player = args.player
                )
            )
        }
        tableLayout.setOnClickListener {
            findNavController().navigate(
                ChooseModeFragmentDirections.actionChooseModeFragmentToTableGameFragment(
                    args.player
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}