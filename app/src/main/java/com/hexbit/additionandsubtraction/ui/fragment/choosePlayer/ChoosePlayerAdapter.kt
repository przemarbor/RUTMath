package com.hexbit.additionandsubtraction.ui.fragment.choosePlayer

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hexbit.additionandsubtraction.R
import com.hexbit.additionandsubtraction.data.model.Player

class ChoosePlayerAdapter(
    private val onItemClickedListener: (Player) -> Unit
) : RecyclerView.Adapter<PlayerViewHolder>() {

    private val playersList = arrayListOf<Player>()

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.player_row, null, false)
        return PlayerViewHolder(
            view,
            onItemClickedListener
        )
    }

    override fun getItemCount(): Int = playersList.size

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(position + 1, playersList[position])
    }

    fun refreshAdapter(list: List<Player>) {
        playersList.apply {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }
}