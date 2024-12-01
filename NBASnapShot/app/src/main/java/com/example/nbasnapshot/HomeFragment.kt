package com.example.nbasnapshot

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Headers
import org.json.JSONObject

private const val SCOREBOARD_API_URL = "https://site.api.espn.com/apis/site/v2/sports/basketball/nba/scoreboard"
val json = Json { ignoreUnknownKeys = true }

class HomeFragment : Fragment() {
    private lateinit var gameAdapter: GameAdapter
    private val gamesList = mutableListOf<DisplayGame>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.games)

        // Set up RecyclerView
        gameAdapter = GameAdapter(requireContext(), gamesList)
        recyclerView.adapter = gameAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        fetchGameData()
        return view
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
                            venueName = competition.venue.fullName
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

    /*private suspend fun parseGamesFromJson(jsonString: String) {
        try {
            // Attempt to decode the JSON into the GameResponse object
            val gameResponse = Json.decodeFromString<GameResponse>(jsonString)

            // Continue processing the data
            gamesList.clear()
            for (event in gameResponse.events) {
                val competition = event.competitions.first()
                val homeTeam = competition.competitors.find { it.homeORAway == "home" }
                val awayTeam = competition.competitors.find { it.homeORAway == "away" }

                val displayGame = DisplayGame(
                    homeTeamName = homeTeam?.team?.displayName ?: "",
                    awayTeamName = awayTeam?.team?.displayName ?: "",
                    homeTeamScore = homeTeam?.score ?: "",
                    awayTeamScore = awayTeam?.score ?: "",
                    homeTeamLogoUrl = homeTeam?.team?.logo ?: "",
                    awayTeamLogoUrl = awayTeam?.team?.logo ?: "",
                    homeTeamRecord = homeTeam?.record?.firstOrNull()?.summary ?: "",
                    awayTeamRecord = awayTeam?.record?.firstOrNull()?.summary ?: "",
                    venueName = competition.venue.fullName
                )
                gamesList.add(displayGame)
            }

            delay(100) // Ensure smooth UI update
            gameAdapter.notifyDataSetChanged()
        } catch (e: kotlinx.serialization.SerializationException) {
            // Specific handling for malformed JSON
            Log.e("HomeFragment", "Serialization error parsing JSON: ${e.message}")
        } catch (e: Exception) {
            Log.e("HomeFragment", "Error parsing JSON: ${e.message}")
        }
    }*/
}