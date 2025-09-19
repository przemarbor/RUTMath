package com.octbit.rutmath.ui.fragment.choosePlayer

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.octbit.rutmath.data.model.Player
import com.octbit.rutmath.databinding.PlayerRowBinding

class ChoosePlayerAdapter(
    private val onItemClickedListener: (Player) -> Unit
) : RecyclerView.Adapter<PlayerViewHolder>() {

    private val playersList = arrayListOf<Player>()

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding = PlayerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayerViewHolder(
            binding,
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