package com.octbit.rutmath.ui.fragment.unitsList

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.octbit.rutmath.R
import com.octbit.rutmath.data.model.ExerciseType
import com.octbit.rutmath.data.model.Operation
import com.octbit.rutmath.databinding.UnitsExerciseItemBinding

class UnitsViewHolder(
    private val binding: UnitsExerciseItemBinding,
    private val clickCallback: (exerciseType: ExerciseType) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    private val TIME = binding.root.resources.getString(R.string.units_time)
    private val LENGTH = binding.root.resources.getString(R.string.units_length)
    private val WEIGHT = binding.root.resources.getString(R.string.units_weight)
    private val SURFACE = binding.root.resources.getString(R.string.units_surface)
    private val ALL = binding.root.resources.getString(R.string.units_all)

    fun bind(exerciseType: ExerciseType) = with(binding){
        title.text = when (exerciseType.operation) {
            Operation.UNITS_TIME -> TIME
            Operation.UNITS_LENGTH -> LENGTH
            Operation.UNITS_WEIGHT -> WEIGHT
            Operation.UNITS_SURFACE -> SURFACE
            Operation.UNITS_ALL -> ALL
            else -> null
        }.plus(" ").plus(((exerciseType.difficulty-1) % 3)+1)
        if (exerciseType.difficulty == 10)
            title.text = title.text.dropLast(2)

        /**
         *  Set a listener on unlocked exercise tile and change its color
         */
        if (exerciseType.isUnlocked) {
            root.setOnClickListener {
                clickCallback.invoke(exerciseType)
            }
            root.background = ContextCompat.getDrawable(root.context, R.drawable.bg_in_tile_exercise)
        }

        val stars = arrayOf(star1, star2, star3, star4, star5)

        // Reset all stars to the default white icon
        stars.forEach { star ->
            star.setImageResource(R.drawable.ic_star)
        }

        for (i in 1..exerciseType.rate) {
            stars[i - 1].setImageResource(R.drawable.ic_star_yellow_24dp)
        }
    }
}