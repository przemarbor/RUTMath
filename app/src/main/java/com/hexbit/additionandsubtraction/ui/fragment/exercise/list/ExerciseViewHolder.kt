package com.hexbit.additionandsubtraction.ui.fragment.exercise.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hexbit.additionandsubtraction.R
import com.hexbit.additionandsubtraction.data.model.ExerciseType
import com.hexbit.additionandsubtraction.data.model.Operation
import kotlinx.android.synthetic.main.exercise_item.view.*

class ExerciseViewHolder(
    private val view: View,
    private val clickCallback: (exerciseType: ExerciseType) -> Unit
) :
    RecyclerView.ViewHolder(view) {

    companion object {
        const val PLUS_VALUE = "+"
        const val MINUS_VALUE = "-"
        const val PLUS_MINUS_VALUE = "+/-"
    }

    fun bind(exerciseType: ExerciseType) {
        view.title.text = when (exerciseType.operation) {
            Operation.PLUS -> PLUS_VALUE
            Operation.MINUS -> MINUS_VALUE
            Operation.PLUS_MINUS -> PLUS_MINUS_VALUE
        }.plus(" ").plus(exerciseType.maxNumber)
        view.setOnClickListener {
            clickCallback.invoke(exerciseType)
        }
        val stars = arrayOf(view.star1, view.star2, view.star3, view.star4, view.star5)
        for (i in 1..exerciseType.rate) {
            stars[i - 1].setImageResource(R.drawable.ic_star_yellow_24dp)
        }
    }
}