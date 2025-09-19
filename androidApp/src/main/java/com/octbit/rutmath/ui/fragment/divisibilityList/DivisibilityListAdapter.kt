package com.octbit.rutmath.ui.fragment.divisibilityList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.octbit.rutmath.data.model.ExerciseType
import com.octbit.rutmath.databinding.DivisibilityExerciseItemBinding

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
        val binding = DivisibilityExerciseItemBinding.inflate(LayoutInflater.from(parent.context))
        return DivisibilityViewHolder(binding, clickCallback)
    }

    override fun getItemCount(): Int = exercises.size

    override fun onBindViewHolder(holder: DivisibilityViewHolder, position: Int) {
        holder.bind(exercises[position])
    }

}