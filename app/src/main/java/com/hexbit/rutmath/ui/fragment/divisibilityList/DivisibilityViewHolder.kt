package com.hexbit.rutmath.ui.fragment.divisibilityList

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hexbit.rutmath.R
import com.hexbit.rutmath.data.model.ExerciseType
import kotlinx.android.synthetic.main.divisibility_exercise_item.view.*

class DivisibilityViewHolder(
    private val view: View,
    private val clickCallback: (exerciseType: ExerciseType) -> Unit
) :
    RecyclerView.ViewHolder(view) {

    companion object {
        const val EASY_VALUE = "Easy"
        const val NORMAL_VALUE = "Normal"
        const val HARD_VALUE = "Hard"
        const val VERY_HARD_VALUE = "Very Hard"
    }

    fun bind(exerciseType: ExerciseType) {
        view.title.text = when (exerciseType.maxNumber) {
            1,2,3 -> EASY_VALUE
            4,5,6 -> NORMAL_VALUE
            9,8,7 -> HARD_VALUE
            10 -> VERY_HARD_VALUE
            else -> null
        }.plus(" ").plus(((exerciseType.maxNumber-1) % 3)+1)
        if (exerciseType.maxNumber == 10)
            view.title.text = view.title.text.dropLast(2)

        /**
         *  Set a listener on unlocked exercise tile and change its color
         */
        if (exerciseType.unlocked) {
            view.setOnClickListener {
                clickCallback.invoke(exerciseType)
            }
            view.background = ContextCompat.getDrawable(view.context, R.drawable.bg_in_tile_exercise)
        }

        val stars = arrayOf(view.star1, view.star2, view.star3, view.star4, view.star5)
        for (i in 1..exerciseType.rate) {
            stars[i - 1].setImageResource(R.drawable.ic_star_yellow_24dp)
        }
    }
}