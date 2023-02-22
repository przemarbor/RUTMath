package com.hexbit.rutmath.ui.fragment.addSubList

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hexbit.rutmath.R
import com.hexbit.rutmath.data.model.ExerciseType
import com.hexbit.rutmath.data.model.Operation
import com.hexbit.rutmath.databinding.NormalExerciseItemBinding

class AddSubViewHolder(private val binding: NormalExerciseItemBinding, private val clickCallback: (exerciseType: ExerciseType) -> Unit)
    : RecyclerView.ViewHolder(binding.root) {

    companion object {
        const val PLUS_VALUE = "+"
        const val MINUS_VALUE = "-"
        const val PLUS_MINUS_VALUE = "+/-"
    }

    fun bind(exerciseType: ExerciseType) = with(binding){
        title.text = when (exerciseType.operation) {
            Operation.PLUS -> PLUS_VALUE
            Operation.MINUS -> MINUS_VALUE
            Operation.PLUS_MINUS -> PLUS_MINUS_VALUE
            else -> null
        }.plus(" ").plus(exerciseType.difficulty)

        // Set a listener on unlocked exercise tile and change its color
        if (exerciseType.isUnlocked) {
            root.setOnClickListener {
                clickCallback.invoke(exerciseType)
            }
            root.background = ContextCompat.getDrawable(root.context,R.drawable.bg_in_tile_exercise)
        }

        val stars = arrayOf(binding.star1, binding.star2, binding.star3, binding.star4, binding.star5)
        for (i in 1..exerciseType.rate) {
            stars[i - 1].setImageResource(R.drawable.ic_star_yellow_24dp)
        }
    }
}