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

    private val difficultyList = listOf(
        view.resources.getString(R.string.divisibility_difficulty_1),
        view.resources.getString(R.string.divisibility_difficulty_2),
        view.resources.getString(R.string.divisibility_difficulty_3),
        view.resources.getString(R.string.divisibility_difficulty_4)
    )

    fun bind(exerciseType: ExerciseType) {
        view.title.text = when (exerciseType.difficulty) {
            1,2,3 -> difficultyList[0]
            4,5,6 -> difficultyList[1]
            9,8,7 -> difficultyList[2]
            10 -> difficultyList[3]
            else -> null
        }.plus(" ").plus(((exerciseType.difficulty-1) % 3)+1)
        if (exerciseType.difficulty == 10)
            view.title.text = view.title.text.dropLast(2)

        /**
         *  Set a listener on unlocked exercise tile and change its color
         */
        if (exerciseType.isUnlocked) {
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