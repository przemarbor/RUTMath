package com.hexbit.rutmath.ui.fragment.divisibilityList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hexbit.rutmath.R
import com.hexbit.rutmath.data.model.ExerciseType

/**
 * Adapter that contains divisibility exercise list.
 *
 */
class DivisibilityListAdapter(
    private val exercises: ArrayList<ExerciseType>,
    private val clickCallback: (exerciseType: ExerciseType) -> Unit
) : RecyclerView.Adapter<DivisibilityViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DivisibilityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.divisibility_exercise_item, null, false)
        return DivisibilityViewHolder(view, clickCallback)
    }

    override fun getItemCount(): Int = exercises.size

    override fun onBindViewHolder(holder: DivisibilityViewHolder, position: Int) {
        holder.bind(exercises[position])
    }

}