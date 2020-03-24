package com.hexbit.rutmath.ui.fragment.scoreboard

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hexbit.rutmath.R
import com.hexbit.rutmath.data.model.Score

class ScoreboardAdapter(private val scoresList: List<Score>) :
    RecyclerView.Adapter<ScoreboardViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreboardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.score_row, null, false)
        return ScoreboardViewHolder(view)
    }

    override fun getItemCount(): Int = scoresList.size

    override fun onBindViewHolder(holder: ScoreboardViewHolder, position: Int) {
        holder.bind(position + 1, scoresList[position])
    }
}