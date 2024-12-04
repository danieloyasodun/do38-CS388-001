package com.example.nbasnapshot

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.fragment.app.activityViewModels

class TeamDetailsActivity : AppCompatActivity() {

    private lateinit var teamImageView: ImageView
    private lateinit var teamNameTextView: TextView
    private lateinit var standingSummaryTextView: TextView
    private lateinit var awayRecordTextView: TextView
    private lateinit var homeRecordTextView: TextView
    private lateinit var playoffSeedTextView: TextView
    private lateinit var pointsScoredTextView: TextView
    private lateinit var pointsAllowedTextView: TextView
    private lateinit var buyTicketsButton: Button
    private lateinit var nextEventTextView: TextView
    private lateinit var streakTextView: TextView
    private lateinit var rosterRecyclerView: RecyclerView
    private lateinit var rosterAdapter: RosterAdapter
    private val favoritePlayers = mutableListOf<AthleteInfo>()
    private lateinit var favoritesViewModel: FavoritesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_details)

        // Initialize views
        teamImageView = findViewById(R.id.teamImageView)
        teamNameTextView = findViewById(R.id.nbaTeams)
        standingSummaryTextView = findViewById(R.id.standingSummaryTextView)
        awayRecordTextView = findViewById(R.id.awayRecordTextView)
        homeRecordTextView = findViewById(R.id.homeRecordTextView)
        playoffSeedTextView = findViewById(R.id.playoffSeedTextView)
        pointsScoredTextView = findViewById(R.id.pointsScoredTextView)
        pointsAllowedTextView = findViewById(R.id.pointsAllowedTextView)
        buyTicketsButton = findViewById(R.id.buyTicketsButton)
        nextEventTextView = findViewById(R.id.nextEventTextView)
        streakTextView = findViewById(R.id.streakTextView)

        // Set up the transition and load the image
        val teamName = intent.getStringExtra("TEAM_NAME")
        val imageUrl = intent.getStringExtra("IMAGE_URL")
        val standingSummary = intent.getStringExtra("STANDING_SUMMARY")
        val homeRecordSummary = intent.getStringExtra("HOME_RECORD_SUMMARY")
        val awayRecordSummary = intent.getStringExtra("AWAY_RECORD_SUMMARY")
        val avgPointsFor = intent.getStringExtra("AVG_POINTS_FOR")
        val avgPointsAgainst = intent.getStringExtra("AVG_POINTS_AGAINST")
        val playoffSeed = intent.getStringExtra("PLAYOFF_SEED")?.toDoubleOrNull()?.toInt() ?: 0
        val ticketLink = intent.getStringExtra("TICKET_LINK")
        val nextEvent = intent.getStringExtra("NEXT_EVENT")
        val streak = intent.getStringExtra("STREAK")
        val abbr = intent.getStringExtra("ABBR")
        val color = intent.getStringExtra("COLOR")
        val alternateColor = intent.getStringExtra("ALTERNATE_COLOR")
        val formattedColor = "#$color"
        val formattedAlternateColor = "#$alternateColor"

        rosterRecyclerView = findViewById(R.id.rosterRecyclerView)
        rosterRecyclerView.layoutManager = LinearLayoutManager(this)

        favoritesViewModel = ViewModelProvider(this).get(FavoritesViewModel::class.java)

        teamNameTextView.text = teamName
        standingSummaryTextView.text = standingSummary
        awayRecordTextView.text = "Away Record: $awayRecordSummary"
        homeRecordTextView.text = "Home Record: $homeRecordSummary"
        playoffSeedTextView.text = "Playoff Seed:  $playoffSeed"
        pointsScoredTextView.text = "Points Scored: $avgPointsFor"
        pointsAllowedTextView.text = "Points Allowed: $avgPointsAgainst"
        nextEventTextView.text = "Next Event: $nextEvent"

        val streakText = streak?.toDoubleOrNull()?.let {
            if (it > 0) "Streak: W${it.toInt()}"
            else if (it < 0) "Streak: L${-it.toInt()}"
            else "Streak: None"
        } ?: "Streak: Unknown"

        streakTextView.text = streakText

        // Use Glide to load the image (same as before)
        Glide.with(this)
            .load(imageUrl)
            .into(teamImageView)

        teamNameTextView.setTextColor(Color.parseColor(formattedColor))
        buyTicketsButton.setTextColor(Color.parseColor(formattedAlternateColor))
        buyTicketsButton.setBackgroundColor(Color.parseColor(formattedColor))

        // Check if ticketLink is valid
        if (!ticketLink.isNullOrEmpty()) {
            buyTicketsButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(ticketLink))
                startActivity(intent)
            }
        } else {
            buyTicketsButton.setOnClickListener {
                Toast.makeText(this, "No ticket link available", Toast.LENGTH_SHORT).show()
            }
        }

        abbr?.let { fetchRoster(it) }

    }

    override fun onStart() {
        super.onStart()

        // Set up the shared element transition
        val sharedElementTransition = android.transition.TransitionInflater.from(this)
            .inflateTransition(android.R.transition.move)
        window.sharedElementEnterTransition = sharedElementTransition
    }

    private fun updateRosterUI(athletes: List<AthleteInfo>) {
        rosterAdapter = RosterAdapter(athletes) { athlete ->
            // Handle the long press and add player to favorites
            onPlayerLongPressed(athlete)
        }
        rosterRecyclerView.adapter = rosterAdapter
    }

    private fun onPlayerLongPressed(athlete: AthleteInfo) {
        favoritesViewModel.addPlayerToFavorites(athlete) // Add the player to the ViewModel
        favoritePlayers.add(athlete) // Add the player to the list of favorite players
        Toast.makeText(this, "${athlete.displayName} added to favorites!", Toast.LENGTH_SHORT).show()
    }

    private fun fetchRoster(abbr: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://site.api.espn.com/apis/site/v2/sports/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        apiService.getRoster(abbr).enqueue(object : Callback<RosterResponse> {
            override fun onResponse(call: Call<RosterResponse>, response: Response<RosterResponse>) {
                if (response.isSuccessful) {
                    val roster = response.body()
                    // Update UI with roster data
                    roster?.athletes?.let { athletes ->
                        // Handle roster data (for example, display names or details)
                        updateRosterUI(athletes)
                    }
                } else {
                    Toast.makeText(this@TeamDetailsActivity, "Failed to load roster", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RosterResponse>, t: Throwable) {
                Toast.makeText(this@TeamDetailsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
