package com.octbit.rutmath.ui.fragment.mulDivList

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.octbit.rutmath.R
import com.octbit.rutmath.data.model.ExerciseType
import com.octbit.rutmath.data.model.Operation
import com.octbit.rutmath.databinding.NormalExerciseItemBinding

class MulDivViewHolder(
    private val binding: NormalExerciseItemBinding,
    private val clickCallback: (exerciseType: ExerciseType) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        const val MULTIPLY_VALUE = "×"
        const val DIVIDE_VALUE = "÷"
        const val MULTIPLY_DIVIDE_VALUE = "×/÷"

//        const val  NEGATIVE_PLUS_MUL_VALUE="×+(-)"
//        const val  NEGATIVE_MINUS_MUL_VALUE="×-(-)"
//        const val  NEGATIVE_PLUS_DIV_VALUE="÷+(-)"
//        const val  NEGATIVE_MINUS_DIV_VALUE="÷-(-)"
//
//        const val NEGATIVE_PLUS_MINUS_MULTIPLY_VALUE = "×±(-)"
//        const val NEGATIVE_PLUS_MINUS_DIVIDE_VALUE = "÷±(-)"
        const val NEGATIVE_MUL = "× -"
        const val NEGATIVE_DIV = "÷ -"
        const val NEGATIVE_MUL_DIV = "×/÷ -"


    }

    fun bind(exerciseType: ExerciseType) = with(binding){
        title.text = when (exerciseType.operation) {
            Operation.MULTIPLY-> MULTIPLY_VALUE
            Operation.DIVIDE-> DIVIDE_VALUE
            Operation.MULTIPLY_DIVIDE -> MULTIPLY_DIVIDE_VALUE

            Operation.NEGATIVE_MUL -> NEGATIVE_MUL
            Operation.NEGATIVE_DIV -> NEGATIVE_DIV
            Operation.NEGATIVE_MUL_DIV -> NEGATIVE_MUL_DIV
            //new code here
//            Operation.NEGATIVE_PLUS_MUL->NEGATIVE_PLUS_MUL_VALUE
//            Operation.NEGATIVE_MINUS_MUL-> NEGATIVE_MINUS_MUL_VALUE
//            Operation.NEGATIVE_PLUS_DIV->NEGATIVE_PLUS_DIV_VALUE
//            Operation.NEGATIVE_MINUS_DIV-> NEGATIVE_MINUS_DIV_VALUE
//            Operation.NEGATIVE_PLUS_MINUS_MUL-> NEGATIVE_PLUS_MINUS_MULTIPLY_VALUE
//            Operation.NEGATIVE_PLUS_MINUS_DIV-> NEGATIVE_PLUS_MINUS_DIVIDE_VALUE
            else -> null
        }.plus(" ").plus(exerciseType.difficulty)

        /**
         *  Set a listener on unlocked exercise tile and change its color
         */

        if (exerciseType.isUnlocked) {
            root.setOnClickListener {
                clickCallback.invoke(exerciseType)
            }
            root.background = ContextCompat.getDrawable(root.context,R.drawable.bg_in_tile_exercise)
        }
        else
        {
            root.setOnClickListener(null)
            root.background = ContextCompat.getDrawable(root.context,R.drawable.bg_in_tile_exercise_disabled)
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