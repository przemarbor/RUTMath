package com.hexbit.rutmath.ui.fragment.scoreboard

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hexbit.rutmath.data.model.Score
import com.hexbit.rutmath.databinding.ScoreRowBinding

class ScoreboardAdapter(private val scoresList: List<Score>) :
    RecyclerView.Adapter<ScoreboardViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreboardViewHolder {
        val binding = ScoreRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScoreboardViewHolder(binding)
    }

    override fun getItemCount(): Int = scoresList.size

    override fun onBindViewHolder(holder: ScoreboardViewHolder, position: Int) {
        holder.bind(position + 1, scoresList[position])
    }
}