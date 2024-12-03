package com.example.nbasnapshot

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class GameAdapter(private val context: Context, private val displayGames: MutableList<DisplayGame>):
    RecyclerView.Adapter<GameAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val awayTeamImageView = itemView.findViewById<ImageView>(R.id.awayTeamImage)
        private val homeTeamImageView = itemView.findViewById<ImageView>(R.id.teamImage)
        private val awayTeamNameTextView = itemView.findViewById<TextView>(R.id.awayTeamName)
        private val homeTeamNameTextView = itemView.findViewById<TextView>(R.id.homeTeamName)
        private val awayRecordTextView = itemView.findViewById<TextView>(R.id.awayRecord)
        private val homeRecordTextView = itemView.findViewById<TextView>(R.id.homeRecord)
        private val awayScoreTextView = itemView.findViewById<TextView>(R.id.awayScore)
        private val homeScoreTextView = itemView.findViewById<TextView>(R.id.homeScore)
        private val venueNameTextView = itemView.findViewById<TextView>(R.id.venueName)
        private val gameClockTextView = itemView.findViewById<TextView>(R.id.gameClock)
        private val gameQuarterTextView = itemView.findViewById<TextView>(R.id.gameQuarter)


        init {
            itemView.setOnClickListener(this)
        }

        fun bind(displayGame: DisplayGame) {
            awayTeamNameTextView.text = displayGame.awayTeamName
            homeTeamNameTextView.text = displayGame.homeTeamName
            awayRecordTextView.text = displayGame.awayTeamRecord
            homeRecordTextView.text = displayGame.homeTeamRecord
            awayScoreTextView.text = displayGame.awayTeamScore
            homeScoreTextView.text = displayGame.homeTeamScore
            venueNameTextView.text = displayGame.venueName
            gameClockTextView.text = displayGame.displayClock
            gameQuarterTextView.text = "Quarter: " + displayGame.period.toString()

            // Handle game clock and quarter visibility
            if (displayGame.displayClock == "0.0" && displayGame.period == 0) {
                // Game not yet started
                gameQuarterTextView.text = "Game not yet started"
                gameClockTextView.visibility = View.INVISIBLE
                gameQuarterTextView.visibility = View.VISIBLE
            } else if (displayGame.displayClock == "0.0" && displayGame.period == 2) {
                // Set "Half Time" and hide the clock
                gameQuarterTextView.text = "Half Time"
                gameClockTextView.visibility = View.INVISIBLE
                gameQuarterTextView.visibility = View.VISIBLE
            } else if (displayGame.displayClock == "0.0" && displayGame.period == 4) {
                // If it's the end of the 4th quarter
                gameQuarterTextView.text = "Final"
                gameClockTextView.visibility = View.INVISIBLE
                gameQuarterTextView.visibility = View.VISIBLE
            } else if (displayGame.displayClock == "0.0" && displayGame.period != 2 && displayGame.period != 4) {
                // If it's the end of a quarter but not halftime or the 4th quarter
                gameQuarterTextView.text = "Quarter: " + displayGame.period.toString()
                gameClockTextView.visibility = View.INVISIBLE
                gameQuarterTextView.visibility = View.VISIBLE
            } else {
                // Make both clock and quarter text visible
                gameClockTextView.visibility = View.VISIBLE
                gameQuarterTextView.visibility = View.VISIBLE
            }

            // Load team images with Glide
            Glide.with(context)
                .load(displayGame.awayTeamLogoUrl)
                .into(awayTeamImageView)

            Glide.with(context)
                .load(displayGame.homeTeamLogoUrl)
                .into(homeTeamImageView)
        }


        override fun onClick(p0: View?) {
            val game = displayGames[absoluteAdapterPosition]
            val awayLeaders = game.leaders.takeLast(game.leaders.size / 2)
            val homeLeaders = game.leaders.take(game.leaders.size / 2)

            val intent = Intent(context, GameDetailsActivity::class.java)

            intent.putExtra("AWAY_TEAM_NAME", game.awayTeamName)
            intent.putExtra("HOME_TEAM_NAME", game.homeTeamName)
            intent.putExtra("AWAY_TEAM_SCORE", game.awayTeamScore)
            intent.putExtra("HOME_TEAM_SCORE", game.homeTeamScore)
            intent.putExtra("AWAY_TEAM_LOGO_URL", game.awayTeamLogoUrl)
            intent.putExtra("HOME_TEAM_LOGO_URL", game.homeTeamLogoUrl)
            intent.putExtra("AWAY_TEAM_RECORD", game.awayTeamRecord)
            intent.putExtra("HOME_TEAM_RECORD", game.homeTeamRecord)
            intent.putExtra("GAME_HEADER", game.shortName)

            // Pass display clock and period
            intent.putExtra("DISPLAY_CLOCK", game.displayClock)
            intent.putExtra("PERIOD", game.period)

            // Pass line scores (serialize the list)
            intent.putExtra("LINE_SCORES", ArrayList(game.lineScores))

            // Pass leaders (serialize the list)
            intent.putParcelableArrayListExtra("AWAY_TEAM_LEADERS", ArrayList(awayLeaders)) // Ensure this list is not null
            intent.putParcelableArrayListExtra("HOME_TEAM_LEADERS", ArrayList(homeLeaders)) // Ensure this list is not null



            context.startActivity(intent)
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_games, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val games = displayGames[position]
        holder.bind(games)
    }

    override fun getItemCount() = displayGames.size
}