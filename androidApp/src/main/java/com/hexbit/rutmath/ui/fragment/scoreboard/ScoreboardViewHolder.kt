package com.hexbit.rutmath.ui.fragment.scoreboard

import androidx.recyclerview.widget.RecyclerView
import com.hexbit.rutmath.R
import com.hexbit.rutmath.data.model.Score
import com.hexbit.rutmath.databinding.ScoreRowBinding

class ScoreboardViewHolder(
    private val binding: ScoreRowBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(position: Int, score: Score) = with(binding) {
        nick.text = root.context.getString(R.string.scoreboard_format, position, score.nick)
        binding.score.text = score.score.toString()
    }
}