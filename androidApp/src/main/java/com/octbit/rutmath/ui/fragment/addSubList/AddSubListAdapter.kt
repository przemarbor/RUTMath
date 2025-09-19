package com.octbit.rutmath.ui.fragment.addSubList

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import com.octbit.rutmath.data.model.ExerciseType
import com.octbit.rutmath.databinding.NormalExerciseItemBinding

/**
 * Adapter that contains exercise list.
 *
 */
class AddSubListAdapter(
    private val exercises: ArrayList<ExerciseType>,
    private val clickCallback: (exerciseType: ExerciseType) -> Unit,
) : RecyclerView.Adapter<AddSubViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddSubViewHolder {
        val binding = NormalExerciseItemBinding.inflate(LayoutInflater.from(parent.context))
        return AddSubViewHolder(binding, clickCallback)
    }
    override fun getItemCount(): Int = exercises.size

    override fun onBindViewHolder(holder: AddSubViewHolder, position: Int) {
        holder.bind(exercises[position])
    }

}