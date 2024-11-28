package com.example.nbasnapshot

import android.content.Context
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
        private val homeTeamImageView = itemView.findViewById<ImageView>(R.id.homeTeamImage)
        private val awayTeamNameTextView = itemView.findViewById<TextView>(R.id.awayTeamName)
        private val homeTeamNameTextView = itemView.findViewById<TextView>(R.id.homeTeamName)
        private val awayRecordTextView = itemView.findViewById<TextView>(R.id.awayRecord)
        private val homeRecordTextView = itemView.findViewById<TextView>(R.id.homeRecord)
        private val awayScoreTextView = itemView.findViewById<TextView>(R.id.awayScore)
        private val homeScoreTextView = itemView.findViewById<TextView>(R.id.homeScore)
        private val venueNameTextView = itemView.findViewById<TextView>(R.id.venueName)


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

            Glide.with(context)
                .load(displayGame.awayTeamLogoUrl)
                .into(awayTeamImageView)

            Glide.with(context)
                .load(displayGame.homeTeamLogoUrl)
                .into(homeTeamImageView)
        }

        override fun onClick(p0: View?) {
            val game = displayGames[absoluteAdapterPosition]
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