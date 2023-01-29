package com.hexbit.rutmath.ui.fragment.mulDivList

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hexbit.rutmath.R
import com.hexbit.rutmath.data.model.ExerciseType
import com.hexbit.rutmath.data.model.Operation
import kotlinx.android.synthetic.main.normal_exercise_item.view.*

class MulDivViewHolder(
    private val view: View,
    private val clickCallback: (exerciseType: ExerciseType) -> Unit
) :
    RecyclerView.ViewHolder(view) {

    companion object {
        const val MULTIPLY_VALUE = "×"
        const val DIVIDE_VALUE = "÷"
        const val MULTIPLY_DIVIDE_VALUE = "×/÷"
    }

    fun bind(exerciseType: ExerciseType) {
        view.title.text = when (exerciseType.operation) {
            Operation.MULTIPLY -> MULTIPLY_VALUE
            Operation.DIVIDE -> DIVIDE_VALUE
            Operation.MULTIPLY_DIVIDE -> MULTIPLY_DIVIDE_VALUE
            else -> null
        }.plus(" ").plus(exerciseType.difficulty)

        /**
         *  Set a listener on unlocked exercise tile and change its color
         */
        if (exerciseType.isUnlocked) {
            view.setOnClickListener {
                clickCallback.invoke(exerciseType)
            }
            view.background = ContextCompat.getDrawable(view.context,R.drawable.bg_in_tile_exercise)
        }

        val stars = arrayOf(view.star1, view.star2, view.star3, view.star4, view.star5)
        for (i in 1..exerciseType.rate) {
            stars[i - 1].setImageResource(R.drawable.ic_star_yellow_24dp)
        }
    }
}