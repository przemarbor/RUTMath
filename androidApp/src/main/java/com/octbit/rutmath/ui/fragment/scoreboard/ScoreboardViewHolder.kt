package com.octbit.rutmath.ui.fragment.scoreboard

import androidx.recyclerview.widget.RecyclerView
import com.octbit.rutmath.R
import com.octbit.rutmath.data.model.Score
import com.octbit.rutmath.databinding.ScoreRowBinding

class ScoreboardViewHolder(
    private val binding: ScoreRowBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(position: Int, score: Score) = with(binding) {
        nick.text = root.context.getString(R.string.scoreboard_format, position, score.nick)
        binding.score.text = score.score.toString()
    }
}