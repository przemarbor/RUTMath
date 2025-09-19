package com.octbit.rutmath.ui.fragment.addSubList

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.octbit.rutmath.R
import com.octbit.rutmath.data.model.ExerciseType
import com.octbit.rutmath.data.model.Operation
import com.octbit.rutmath.databinding.NormalExerciseItemBinding

class AddSubViewHolder(private val binding: NormalExerciseItemBinding, private val clickCallback: (exerciseType: ExerciseType) -> Unit)
    : RecyclerView.ViewHolder(binding.root) {

    companion object {
        const val PLUS_VALUE = "+"
        const val MINUS_VALUE = "-"
        const val PLUS_MINUS_VALUE = "±"
        // new code here for negative number
        const val  NEGATIVE_PLUS_VALUE="+(-)"
        const val  NEGATIVE_MINUS_VALUE="-(-)"
        const val  NEGATIVE_PLUS_MINUS_VALUE="±(-)"
    }

    fun bind(exerciseType: ExerciseType) = with(binding) {
        title.text = when (exerciseType.operation) {
            Operation.PLUS -> PLUS_VALUE
            Operation.MINUS -> MINUS_VALUE
            Operation.PLUS_MINUS -> PLUS_MINUS_VALUE
            Operation.NEGATIVE_PLUS -> NEGATIVE_PLUS_VALUE
            Operation.NEGATIVE_MINUS -> NEGATIVE_MINUS_VALUE
            Operation.NEGATIVE_PLUS_MINUS -> NEGATIVE_PLUS_MINUS_VALUE
            else -> null
        }.plus(" ").plus(exerciseType.difficulty)

        // Set a listener on unlocked exercise tile and change its color
        if (exerciseType.isUnlocked) {
            root.setOnClickListener {
                clickCallback.invoke(exerciseType)
            }
            root.background = ContextCompat.getDrawable(root.context, R.drawable.bg_in_tile_exercise)
        } else {
            root.setOnClickListener(null)
            root.background = ContextCompat.getDrawable(root.context, R.drawable.bg_in_tile_exercise_disabled)
        }

        val stars = arrayOf(binding.star1, binding.star2, binding.star3, binding.star4, binding.star5)
        // Reset all stars to the default white icon
        stars.forEach { star ->
            star.setImageResource(R.drawable.ic_star) // Use your default star icon
        }
        for (i in 1..exerciseType.rate.coerceIn(0, 5)) {
            stars[i - 1].setImageResource(R.drawable.ic_star_yellow_24dp)
        }
    }
}