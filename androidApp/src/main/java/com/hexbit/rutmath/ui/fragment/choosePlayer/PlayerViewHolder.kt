package com.octbit.rutmath.ui.fragment.choosePlayer

import androidx.recyclerview.widget.RecyclerView
import com.octbit.rutmath.R
import com.octbit.rutmath.data.model.Player
import com.octbit.rutmath.databinding.PlayerRowBinding

class PlayerViewHolder(
    private val binding: PlayerRowBinding,
    private val onItemClickedListener: (Player) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(position: Int, player: Player) = with(binding){
        root.setOnClickListener {
            onItemClickedListener.invoke(player)
        }
        nick.text = root.context.getString(R.string.scoreboard_format, position, player.nick)
    }
}