package com.example.nbasnapshot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class StandingsAdapter (private val teams: List<TeamEntity>)
    : RecyclerView.Adapter<StandingsAdapter.TeamViewHolder>() {
    inner class TeamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val teamImageView: ImageView = itemView.findViewById(R.id.standingTeamImage)
        private val teamNameTextView: TextView = itemView.findViewById(R.id.standingTeamName)
        private val teamRecordTextView: TextView = itemView.findViewById(R.id.standingTeamRecord)
        private val playoffSeedTextView: TextView = itemView.findViewById(R.id.playoffSeed)

        fun bind(team: TeamEntity) {
            teamNameTextView.text = team.name
            teamRecordTextView.text = team.record
            playoffSeedTextView.text = team.playoffSeed.toString()


            Glide.with(itemView.context)
                .load(team.logoUrl)
                .into(teamImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_standing, parent, false)
        return TeamViewHolder(view)
    }

    override fun getItemCount() = teams.size

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        val team = teams[position]
        holder.bind(team)
    }
}