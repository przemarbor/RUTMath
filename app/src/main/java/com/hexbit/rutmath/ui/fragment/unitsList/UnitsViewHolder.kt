package com.hexbit.rutmath.ui.fragment.unitsList

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hexbit.rutmath.R
import com.hexbit.rutmath.data.model.ExerciseType
import com.hexbit.rutmath.data.model.Operation
import kotlinx.android.synthetic.main.units_exercise_item.view.*

class UnitsViewHolder(
    private val view: View,
    private val clickCallback: (exerciseType: ExerciseType) -> Unit
) :
    RecyclerView.ViewHolder(view) {

    private val TIME = view.resources.getString(R.string.units_time)
    private val LENGTH = view.resources.getString(R.string.units_length)
    private val WEIGHT = view.resources.getString(R.string.units_weight)
    private val SURFACE = view.resources.getString(R.string.units_surface)
    private val ALL = view.resources.getString(R.string.units_all)

    fun bind(exerciseType: ExerciseType) {
        view.title.text = when (exerciseType.operation) {
            Operation.UNITS_TIME -> TIME
            Operation.UNITS_LENGTH -> LENGTH
            Operation.UNITS_WEIGHT -> WEIGHT
            Operation.UNITS_SURFACE -> SURFACE
            Operation.UNITS_ALL -> ALL
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