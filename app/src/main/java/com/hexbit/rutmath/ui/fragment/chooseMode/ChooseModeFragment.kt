package com.hexbit.rutmath.ui.fragment.chooseMode

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hexbit.rutmath.util.base.BaseFragment
import com.hexbit.rutmath.R
import com.hexbit.rutmath.ui.view.TableRateDialog
import kotlinx.android.synthetic.main.fragment_choose_mode.*


class ChooseModeFragment : BaseFragment() {
    override val layout: Int = R.layout.fragment_choose_mode

    private val args: ChooseModeFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(
                ChooseModeFragmentDirections.actionChooseModeFragmentToChoosePlayerFragment()
            )
        }
        args.res.let {
            if (args.res >= 0){
                TableRateDialog(context!!, args.res).show() }}

        initButtons()
    }

    private fun initButtons(){
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
                    player = args.player
                )
            )
        }
    }
}