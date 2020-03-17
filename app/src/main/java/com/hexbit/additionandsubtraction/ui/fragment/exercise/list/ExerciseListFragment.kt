package com.hexbit.additionandsubtraction.ui.fragment.exercise.list


import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.hexbit.additionandsubtraction.R
import com.hexbit.additionandsubtraction.data.model.ExerciseType
import com.hexbit.additionandsubtraction.ui.view.GridSpacingItemDecoration
import com.hexbit.additionandsubtraction.ui.view.RateDialog
import com.hexbit.additionandsubtraction.util.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_exercise_list.*
import org.koin.android.ext.android.inject

class ExerciseListFragment : BaseFragment() {

    override val layout = R.layout.fragment_exercise_list

    companion object {
        private const val COLUMNS_COUNT = 3
    }

    private val args: ExerciseListFragmentArgs by navArgs()

    private val viewModel: ExerciseListViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()

        args.exerciseType?.let {
            if (args.rate > 0) {
                viewModel.updateExerciseType(it.copy(rate = args.rate), args.player.nick)
                RateDialog(context!!, args.rate).show()
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
            adapter = ExerciseAdapter(ArrayList(list), ::exerciseClickCallback)
        }
    }

    /**
     * Triggered when tile with some exerciseType is clicked.
     *
     * Metoda uruchamiana w momencie kliknięcia na dane zadanie z listy.
     */
    private fun exerciseClickCallback(exerciseType: ExerciseType) {
        val direction =
            ExerciseListFragmentDirections.actionExerciseListToNormalGameFragment(
                exerciseType,
                args.player
            )
        findNavController().navigate(direction)
    }
}
