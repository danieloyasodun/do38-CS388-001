package com.example.nbasnapshot

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import androidx.core.app.SharedElementCallback
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class TeamDetailsActivity : AppCompatActivity() {

    private lateinit var teamImageView: ImageView
    private lateinit var teamNameTextView: TextView
    private lateinit var standingSummaryTextView: TextView
    private lateinit var awayRecordTextView: TextView
    private lateinit var homeRecordTextView: TextView
    private lateinit var playoffSeedTextView: TextView
    private lateinit var pointsScoredTextView: TextView
    private lateinit var pointsAllowedTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_details)

        // Initialize views
        teamImageView = findViewById(R.id.teamImageView)
        teamNameTextView = findViewById(R.id.teamNameTextView)
        standingSummaryTextView = findViewById(R.id.standingSummaryTextView)
        awayRecordTextView = findViewById(R.id.awayRecordTextView)
        homeRecordTextView = findViewById(R.id.homeRecordTextView)
        playoffSeedTextView = findViewById(R.id.playoffSeedTextView)
        pointsScoredTextView = findViewById(R.id.pointsScoredTextView)
        pointsAllowedTextView = findViewById(R.id.pointsAllowedTextView)

        // Set up the transition and load the image
        val teamName = intent.getStringExtra("TEAM_NAME")
        val imageUrl = intent.getStringExtra("IMAGE_URL")
        val standingSummary = intent.getStringExtra("STANDING_SUMMARY")
        val homeRecordSummary = intent.getStringExtra("HOME_RECORD_SUMMARY")
        val awayRecordSummary = intent.getStringExtra("AWAY_RECORD_SUMMARY")
        val avgPointsFor = intent.getStringExtra("AVG_POINTS_FOR")
        val avgPointsAgainst = intent.getStringExtra("AVG_POINTS_AGAINST")
        val playoffSeed = intent.getStringExtra("PLAYOFF_SEED")
        val ticketLink = intent.getStringExtra("TICKET_LINK")

        teamNameTextView.text = teamName
        standingSummaryTextView.text = standingSummary
        awayRecordTextView.text = "Away Record: $awayRecordSummary"
        homeRecordTextView.text = "Home Record: $homeRecordSummary"
        playoffSeedTextView.text = "Playoff Seed:  $playoffSeed"
        pointsScoredTextView.text = "Points Scored: $avgPointsFor"
        pointsAllowedTextView.text = "Points Allowed: $avgPointsAgainst"

        // Use Glide to load the image (same as before)
        Glide.with(this)
            .load(imageUrl)
            .into(teamImageView)
    }

    override fun onStart() {
        super.onStart()

        // Set up the shared element transition
        val sharedElementTransition = android.transition.TransitionInflater.from(this)
            .inflateTransition(android.R.transition.move)
        window.sharedElementEnterTransition = sharedElementTransition
    }
}