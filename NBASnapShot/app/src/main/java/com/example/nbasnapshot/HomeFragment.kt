package com.example.nbasnapshot

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Headers
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val SCOREBOARD_API_URL = "https://site.api.espn.com/apis/site/v2/sports/basketball/nba/scoreboard"
val json = Json { ignoreUnknownKeys = true }

class HomeFragment : Fragment() {
    private lateinit var gameAdapter: GameAdapter
    private val gamesList = mutableListOf<DisplayGame>()
    private lateinit var dateHeaderTextView: TextView
    private val interval = 500L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.games)

        // Initialize the TextView for the header
        dateHeaderTextView = view.findViewById(R.id.dateHeader)

        // Set the current date in the header
        val currentDate = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(Date())
        dateHeaderTextView.text = "NBA Games for Today: $currentDate" // Update text with current date


        // Set up RecyclerView
        gameAdapter = GameAdapter(requireContext(), gamesList)
        recyclerView.adapter = gameAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        startFetchingGameData()
        return view
    }

    private fun startFetchingGameData() {
        lifecycleScope.launch {
            while (isActive) {
                fetchGameData()
                delay(interval) // Repeat every second
            }
        }
    }

    private fun fetchGameData() {
        val client = AsyncHttpClient()
        client.get(SCOREBOARD_API_URL, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e("HomeFragment", "API call failed: ${throwable?.message}")
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON?) {
                try {
                    // Decode JSON response
                    val gameResponse = createJson().decodeFromString<GameResponse>(json?.jsonObject.toString())
                    val games = gameResponse.events.mapNotNull { event ->
                        val competition = event.competitions.firstOrNull() ?: return@mapNotNull null
                        val homeTeam = competition.competitors.find { it.homeORAway == "home" }
                        val awayTeam = competition.competitors.find { it.homeORAway == "away" }
                        val gameStatus = event.status.type.name
                        val displayClock = event.status.displayClock // Extract display clock
                        val period = event.status.period // Extract period

                        val lineScores = competition.competitors.flatMap { competitor ->
                            competitor.lineScores?.mapIndexed { index, lineScore ->
                                LineScores(
                                    period = index + 1,  // Assuming periods are indexed from 1
                                    awayScore = lineScore.value,
                                    homeScore = lineScore.value
                                )
                            } ?: emptyList()
                        }

                        // Extract leaders
                        val leaders = competition.competitors.flatMap { competitor ->
                            // Check if the 'leaders' field exists and is not null, else use an empty list
                            competitor.leaders?.flatMap { leader ->
                                leader.leaders.map { leaderDetail ->
                                    Leaders(
                                        displayName = leaderDetail.athlete.fullName,
                                        name = leader.name,
                                        value = leaderDetail.value,
                                        imageUrl = leaderDetail.athlete.headshot
                                    )
                                } ?: emptyList() // In case 'leader.leaders' is null, use an empty list
                            } ?: emptyList() // In case 'competitor.leaders' is null, use an empty list
                        }


                        // Map to DisplayGame
                        DisplayGame(
                            homeTeamName = homeTeam?.team?.shortDisplayName.orEmpty(),
                            awayTeamName = awayTeam?.team?.shortDisplayName.orEmpty(),
                            homeTeamScore = homeTeam?.score.orEmpty(),
                            awayTeamScore = awayTeam?.score.orEmpty(),
                            homeTeamLogoUrl = homeTeam?.team?.logo.orEmpty(),
                            awayTeamLogoUrl = awayTeam?.team?.logo.orEmpty(),
                            homeTeamRecord = homeTeam?.record?.firstOrNull()?.summary.orEmpty(),
                            awayTeamRecord = awayTeam?.record?.firstOrNull()?.summary.orEmpty(),
                            venueName = competition.venue.fullName,
                            status = gameStatus,
                            displayClock = displayClock, // Set display clock
                            period = period, // Set period
                            lineScores = lineScores,
                            leaders = leaders,
                            shortName = event.shortName
                        )
                    }

                    // Update UI with parsed data
                    lifecycleScope.launch {
                        gamesList.clear()
                        gamesList.addAll(games)
                        gameAdapter.notifyDataSetChanged()
                    }
                } catch (e: Exception) {
                    Log.e("HomeFragment", "Error parsing JSON: ${e.message}")
                }
            }
        })
    }
}