package com.hexbit.rutmath.ui.fragment.scoreboard

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hexbit.rutmath.R
import com.hexbit.rutmath.data.model.Score
import kotlinx.android.synthetic.main.score_row.view.*

class ScoreboardViewHolder(
    private val view: View
) : RecyclerView.ViewHolder(view) {

    fun bind(position: Int, score: Score) {
        view.nick.text = view.context.getString(R.string.scoreboard_format, position, score.nick)
        view.score.text = score.score.toString()
    }
}