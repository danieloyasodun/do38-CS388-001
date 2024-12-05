package com.example.nbasnapshot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class LeaderAdapter(private val leaders: List<Leaders>) : RecyclerView.Adapter<LeaderAdapter.LeaderViewHolder>() {

    inner class LeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val leaderImage: ImageView = itemView.findViewById(R.id.leaderImage)
        private val leaderName: TextView = itemView.findViewById(R.id.leaderName)
        private val leaderStatName: TextView = itemView.findViewById(R.id.leaderStatName)
        private val leaderStatValue: TextView = itemView.findViewById(R.id.leaderStatValue)

        fun bind(leader: Leaders) {
            leaderName.text = leader.displayName
            leaderStatName.text = leader.name
            leaderStatValue.text = String.format("%.1f", leader.value)

            // Load the leader image
            Glide.with(itemView.context)
                .load(leader.imageUrl)
                .into(leaderImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game_leaders, parent, false)
        return LeaderViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeaderViewHolder, position: Int) {
        val leader = leaders[position]
        holder.bind(leader)
    }

    override fun getItemCount() = leaders.size
}
