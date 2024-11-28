package com.example.nbasnapshot

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

private const val TAG = "TeamAdapter"

class TeamAdapter(private val context: Context, private val displayTeams: MutableList<DisplayTeam>):
RecyclerView.Adapter<TeamAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private val teamImageView = itemView.findViewById<ImageView>(R.id.homeTeamImage)
        private val teamNameTextView = itemView.findViewById<TextView>(R.id.teamName)
        private val recordSummaryTextView: TextView = itemView.findViewById(R.id.teamRecord)

        init {
            itemView.setOnClickListener(this)
        }
        fun bind(displayTeam: DisplayTeam){
            teamNameTextView.text = displayTeam.teamName
            recordSummaryTextView.text = "Record (W-L): " + displayTeam.recordSummary

            Glide.with(context)
                .load(displayTeam.logoUrl)
                .into(teamImageView)
        }

        override fun onClick(p0: View?) {
            val team = displayTeams[absoluteAdapterPosition]

            // Create an Intent to start TeamDetailsActivity
            val intent = Intent(context, TeamDetailsActivity::class.java)


            // Pass the team name (or any other necessary data) to the new activity
            intent.putExtra("TEAM_NAME", team.teamName)
            intent.putExtra("IMAGE_URL", team.logoUrl)
            intent.putExtra("STANDING_SUMMARY", team.standingSummary)
            intent.putExtra("HOME_RECORD_SUMMARY", team.homeRecordSummary)
            intent.putExtra("AWAY_RECORD_SUMMARY", team.awayRecordSummary)
            intent.putExtra("AVG_POINTS_FOR", team.avgPointsFor)
            intent.putExtra("AVG_POINTS_AGAINST", team.avgPointsAgainst)
            intent.putExtra("PLAYOFF_SEED", team.playoffSeed)

            // Set up the shared element transition
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                context as Activity,
                teamImageView, "teamImageTransition" // "teamImageTransition" is the transition name
            )

            // Start the activity
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_team, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = displayTeams.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val teams = displayTeams[position]
        holder.bind(teams)
    }
}