package com.hexbit.additionandsubtraction.ui.fragment.choosePlayer

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hexbit.additionandsubtraction.R
import com.hexbit.additionandsubtraction.data.model.Player
import kotlinx.android.synthetic.main.score_row.view.*

class PlayerViewHolder(
    private val view: View,
    private val onItemClickedListener: (Player) -> Unit
) : RecyclerView.ViewHolder(view) {

    fun bind(position: Int, player: Player) {
        view.setOnClickListener {
            onItemClickedListener.invoke(player)
        }
        view.nick.text = view.context.getString(R.string.scoreboard_format, position, player.nick)
    }
}