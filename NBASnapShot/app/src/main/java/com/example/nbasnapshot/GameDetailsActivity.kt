package com.example.nbasnapshot

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GameDetailsActivity : AppCompatActivity() {
    private lateinit var awayTeamImageView: ImageView
    private lateinit var homeTeamImageView: ImageView
    private lateinit var awayTeamTextView: TextView
    private lateinit var homeTeamTextView: TextView
    private lateinit var awayTeamScoreTextView: TextView
    private lateinit var homeTeamScoreTextView: TextView
    private lateinit var recyclerViewLeft: RecyclerView
    private lateinit var recyclerViewRight: RecyclerView
    private lateinit var awayQuarter1TextView: TextView
    private lateinit var awayQuarter2TextView: TextView
    private lateinit var awayQuarter3TextView: TextView
    private lateinit var awayQuarter4TextView: TextView
    private lateinit var homeQuarter1TextView: TextView
    private lateinit var homeQuarter2TextView: TextView
    private lateinit var homeQuarter3TextView: TextView
    private lateinit var homeQuarter4TextView: TextView
    private lateinit var awayQuarterImageView: ImageView
    private lateinit var homeQuarterImageView: ImageView
    private lateinit var gameHeaderTextView: TextView
    private lateinit var displayClockTextView: TextView
    private lateinit var periodTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_details)

        // Initialize views
        awayTeamImageView = findViewById(R.id.awayTeamImage)
        homeTeamImageView = findViewById(R.id.teamImage)
        awayTeamTextView = findViewById(R.id.awayTeam)
        homeTeamTextView = findViewById(R.id.homeTeam)
        awayTeamScoreTextView = findViewById(R.id.awayTeamScore)
        homeTeamScoreTextView = findViewById(R.id.homeTeamScore)
        recyclerViewLeft = findViewById(R.id.recyclerViewLeft)
        recyclerViewRight = findViewById(R.id.recyclerViewRight)
        awayQuarter1TextView = findViewById(R.id.awayQuarter1)
        awayQuarter2TextView = findViewById(R.id.awayQuarter2)
        awayQuarter3TextView = findViewById(R.id.awayQuarter3)
        awayQuarter4TextView = findViewById(R.id.awayQuarter4)
        homeQuarter1TextView = findViewById(R.id.homeQuarter1)
        homeQuarter2TextView = findViewById(R.id.homeQuarter2)
        homeQuarter3TextView = findViewById(R.id.homeQuarter3)
        homeQuarter4TextView = findViewById(R.id.homeQuarter4)
        awayQuarterImageView = findViewById(R.id.awayQuarterImage)
        homeQuarterImageView = findViewById(R.id.homeQuarterImage)
        gameHeaderTextView = findViewById(R.id.gameHeader)
        displayClockTextView = findViewById(R.id.displayClock)
        periodTextView = findViewById(R.id.gameQuarter)

        // Retrieve data passed from the adapter
        val awayTeamName = intent.getStringExtra("AWAY_TEAM_NAME")
        val homeTeamName = intent.getStringExtra("HOME_TEAM_NAME")
        val awayTeamLogoUrl = intent.getStringExtra("AWAY_TEAM_LOGO_URL")
        val homeTeamLogoUrl = intent.getStringExtra("HOME_TEAM_LOGO_URL")
        val awayTeamScore = intent.getStringExtra("AWAY_TEAM_SCORE")
        val homeTeamScore = intent.getStringExtra("HOME_TEAM_SCORE")
        val gameHeader = intent.getStringExtra("GAME_HEADER")
        val displayClock = intent.getStringExtra("DISPLAY_CLOCK")
        val period = intent.getIntExtra("PERIOD", 0)

        awayTeamTextView.text = awayTeamName
        homeTeamTextView.text = homeTeamName
        awayTeamScoreTextView.text = awayTeamScore
        homeTeamScoreTextView.text = homeTeamScore
        gameHeaderTextView.text = gameHeader
        displayClockTextView.text = displayClock
        periodTextView.text = "Quarter: " + period.toString()

        // Handle game clock and quarter visibility
        if (displayClock == "0.0" && period == 0) {
            // Game not yet started
            displayClockTextView.visibility = View.INVISIBLE
            periodTextView.visibility = View.INVISIBLE
        } else if (displayClock == "0.0" && period == 2) {
            // Set "Half Time" and hide the clock
            periodTextView.text = "Half Time"
            displayClockTextView.visibility = View.INVISIBLE
            periodTextView.visibility = View.VISIBLE
        } else if (displayClock == "0.0" && period == 4) {
            // If it's the end of the 4th quarter
            periodTextView.text = "Final"
            displayClockTextView.visibility = View.INVISIBLE
            periodTextView.visibility = View.VISIBLE
        } else if (displayClock == "0.0" && period != 2 && period != 4) {
            // If it's the end of a quarter but not halftime or the 4th quarter
            periodTextView.text = "Quarter: " + period.toString()
            displayClockTextView.visibility = View.INVISIBLE
            displayClockTextView.visibility = View.VISIBLE
        } else {
            // Make both clock and quarter text visible
            displayClockTextView.visibility = View.VISIBLE
            periodTextView.visibility = View.VISIBLE
        }

        val awayLeaders: ArrayList<Leaders>? = intent.getParcelableArrayListExtra("AWAY_TEAM_LEADERS")
        val homeLeaders: ArrayList<Leaders>? = intent.getParcelableArrayListExtra("HOME_TEAM_LEADERS")

        val lineScores: ArrayList<LineScores>? = intent.getParcelableArrayListExtra("LINE_SCORES")

        if (lineScores != null) {
            // Display quarter scores
            lineScores.forEach { score ->
                when (score.period) {
                    1 -> {
                        awayQuarter1TextView.text = score.awayScore.toString()
                        homeQuarter1TextView.text = score.homeScore.toString()
                    }
                    2 -> {
                        awayQuarter2TextView.text = score.awayScore.toString()
                        homeQuarter2TextView.text = score.homeScore.toString()
                    }
                    3 -> {
                        awayQuarter3TextView.text = score.awayScore.toString()
                        homeQuarter3TextView.text = score.homeScore.toString()
                    }
                    4 -> {
                        awayQuarter4TextView.text = score.awayScore.toString()
                        homeQuarter4TextView.text = score.homeScore.toString()
                    }
                }
            }
        } else {
            Toast.makeText(this, "Failed to load quarter scores", Toast.LENGTH_SHORT).show()
        }

        if (lineScores?.isEmpty() == true) {
            // If lineScores is empty, hide the quarter score views
            awayQuarter1TextView.visibility = View.INVISIBLE
            awayQuarter2TextView.visibility = View.INVISIBLE
            awayQuarter3TextView.visibility = View.INVISIBLE
            awayQuarter4TextView.visibility = View.INVISIBLE
            homeQuarter1TextView.visibility = View.INVISIBLE
            homeQuarter2TextView.visibility = View.INVISIBLE
            homeQuarter3TextView.visibility = View.INVISIBLE
            homeQuarter4TextView.visibility = View.INVISIBLE
        }


        if (awayLeaders == null || homeLeaders == null) {
            Toast.makeText(this, "Failed to load leaders data", Toast.LENGTH_SHORT).show()
            finish() // Exit the activity if data is missing
            return
        }


        recyclerViewLeft.layoutManager = LinearLayoutManager(this)
        recyclerViewLeft.adapter = LeaderAdapter(awayLeaders)

        recyclerViewRight.layoutManager = LinearLayoutManager(this)
        recyclerViewRight.adapter = LeaderAdapter(homeLeaders)

        Glide.with(this)
            .load(awayTeamLogoUrl)
            .into(awayTeamImageView)

        Glide.with(this)
            .load(homeTeamLogoUrl)
            .into(homeTeamImageView)

        Glide.with(this)
            .load(awayTeamLogoUrl)
            .into(awayQuarterImageView)

        Glide.with(this)
            .load(homeTeamLogoUrl)
            .into(homeQuarterImageView)
    }
}