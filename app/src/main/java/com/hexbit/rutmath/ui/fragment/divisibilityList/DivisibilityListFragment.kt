package com.hexbit.rutmath.ui.fragment.divisibilityList

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.hexbit.rutmath.R
import com.hexbit.rutmath.data.model.ExerciseType
import com.hexbit.rutmath.ui.view.GridSpacingItemDecoration
import com.hexbit.rutmath.ui.view.NormalRateDialog
import com.hexbit.rutmath.util.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_exercise_list.*
import org.koin.android.ext.android.inject

class DivisibilityListFragment : BaseFragment() {

    override val layout = R.layout.fragment_exercise_list

    companion object {
        private const val COLUMNS_COUNT = 1
    }

    private val args: DivisibilityListFragmentArgs by navArgs()

    private val viewModel: DivisibilityListViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(
                DivisibilityListFragmentDirections.actionDivisibilityListFragmentToChooseModeFragment(
                    args.player,
                    res = -1
                )
            )
        }
        initViewModel()
        args.exerciseType?.let {
            if (args.rate > 0) {
                viewModel.updateExerciseType(it.copy(rate = args.rate), args.player.nick)
                viewModel.unlockExerciseType(args.player.nick, args.exerciseType!!.operation, args.exerciseType!!.maxNumber)
                NormalRateDialog(context!!, args.rate).show()
            }
        }
        player_name.text = args.player.nick

    }

    private fun initViewModel() {
        viewModel.getExerciseTypes().observe(viewLifecycleOwner, Observer {
            initRecyclerView(it)
        })
        viewModel.loadExercises(args.player.nick)
    }

    private fun initRecyclerView(list: List<ExerciseType>) {
        recyclerView.apply {
            layoutManager = GridLayoutManager(context, COLUMNS_COUNT)
            addItemDecoration(
                GridSpacingItemDecoration(
                    COLUMNS_COUNT,
                    20,
                    true
                )
            ) // add space between each tiles
            adapter = DivisibilityListAdapter(ArrayList(list), ::exerciseClickCallback)

        }
    }

    /**
     * Triggered when tile with some exerciseType is clicked.
     */
    private fun exerciseClickCallback(exerciseType: ExerciseType) {
        val direction =
            DivisibilityListFragmentDirections.actionDivisibilityListFragmentToDivisibilityGameFragment(
                exerciseType,
                args.player
            )
        findNavController().navigate(direction)
    }
}
