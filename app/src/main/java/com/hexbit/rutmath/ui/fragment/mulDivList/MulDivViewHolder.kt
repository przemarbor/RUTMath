package com.hexbit.rutmath.ui.fragment.mulDivList

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hexbit.rutmath.R
import com.hexbit.rutmath.data.model.ExerciseType
import com.hexbit.rutmath.data.model.Operation
import com.hexbit.rutmath.databinding.NormalExerciseItemBinding

class MulDivViewHolder(
    private val binding: NormalExerciseItemBinding,
    private val clickCallback: (exerciseType: ExerciseType) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        const val MULTIPLY_VALUE = "×"
        const val DIVIDE_VALUE = "÷"
        const val MULTIPLY_DIVIDE_VALUE = "×/÷"
    }

    fun bind(exerciseType: ExerciseType) = with(binding){
        title.text = when (exerciseType.operation) {
            Operation.MULTIPLY -> MULTIPLY_VALUE
            Operation.DIVIDE -> DIVIDE_VALUE
            Operation.MULTIPLY_DIVIDE -> MULTIPLY_DIVIDE_VALUE
            else -> null
        }.plus(" ").plus(exerciseType.difficulty)

        /**
         *  Set a listener on unlocked exercise tile and change its color
         */

        /**
         *  Set a listener on unlocked exercise tile and change its color
         */
        if (exerciseType.isUnlocked) {
            root.setOnClickListener {
                clickCallback.invoke(exerciseType)
            }
            root.background = ContextCompat.getDrawable(root.context,R.drawable.bg_in_tile_exercise)
        }

        val stars = arrayOf(star1, star2, star3, star4, star5)
        for (i in 1..exerciseType.rate) {
            stars[i - 1].setImageResource(R.drawable.ic_star_yellow_24dp)
        }
    }
}