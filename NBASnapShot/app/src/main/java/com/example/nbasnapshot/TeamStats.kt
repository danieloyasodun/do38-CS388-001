package com.example.nbasnapshot

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Headers

fun createJson() = Json {
    isLenient = true
    ignoreUnknownKeys = true
    useAlternativeNames = false
}

private const val BASE_TEAM_API_URL = "https://site.api.espn.com/apis/site/v2/sports/basketball/nba/teams/"

private val ABR_LIST = listOf(
    "ATL", "BOS", "BKN", "CHA", "CHI", "CLE", "DAL", "DEN", "DET", "GS",
    "HOU", "IND", "LAC", "LAL", "MEM", "MIA", "MIL", "MIN", "NO", "NY",
    "OKC", "ORL", "PHI", "PHX", "POR", "SAC", "SA", "TOR", "UTAH", "WSH"
)

class TeamStats : Fragment() {
    private lateinit var teamAdapter: TeamAdapter
    private val teamsList = mutableListOf<DisplayTeam>()
    private val favoriteTeams = mutableListOf<DisplayTeam>()
    private val favoriteTeamsViewModel: FavoritesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_team_stats, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.teams)

        // Set up RecyclerView
        teamAdapter = TeamAdapter(requireContext(), teamsList) { team ->
            // Handle long press (add the team to favorites)
            favoriteTeamsViewModel.addTeamToFavorites(team) // Add the team to the ViewModel
            Toast.makeText(requireContext(), "${team.teamName} added to favorites!", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = teamAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        fetchTeamData()

        return view
    }

    private var completedRequests = 0

    private fun onTeamLongPressed(team: DisplayTeam) {
        addTeamToFavorites(team)
    }

    // Function to add the team to the favorites list and show a Toast
    private fun addTeamToFavorites(team: DisplayTeam) {
        if (!favoriteTeams.contains(team)) {
            favoriteTeams.add(team) // Add team to favorites
            Toast.makeText(requireContext(), "${team.teamName} added to favorites!", Toast.LENGTH_SHORT).show()
        } else {
            // Optionally, show a message if the team is already in favorites
            Toast.makeText(requireContext(), "${team.teamName} is already in favorites.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchTeamData() {
        val client = AsyncHttpClient()

        lifecycleScope.launch {
            ABR_LIST.forEach { abbreviation ->
                val teamApiUrl = "$BASE_TEAM_API_URL$abbreviation"

                client.get(teamApiUrl, object : JsonHttpResponseHandler() {
                    override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?) {
                        Log.e(
                            "TeamStatsFragment",
                            "Failed to fetch data for $abbreviation: Status=$statusCode, Response=$response, Error=${throwable?.message}"
                        )
                        checkRequestsComplete()
                    }

                    override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON?) {
                        try {
                            // Parse response and add to list
                            val teamResponse = createJson().decodeFromString<TeamResponse>(json?.jsonObject.toString())
                            val nbaStats = teamResponse.team

                            val newTeam = nbaStats?.let {
                                val overallRecord = nbaStats.record?.items?.find { it.description == "Overall Record" }
                                val statsList = overallRecord?.stats
                                val playoffSeed = statsList?.find { it.name == "playoffSeed" }?.value ?: 0

                                TeamEntity(
                                    abbreviation = nbaStats.abbreviation,
                                    name = nbaStats.name,
                                    logoUrl = nbaStats.logos[0].logoUrl,
                                    record = overallRecord?.summary ?: "N/A",
                                    winPercentage = statsList?.find { it.name == "winPercent" }?.value?.toString() ?: "0.0",
                                    playoffSeed = playoffSeed.toInt()
                                )
                            }

                            lifecycleScope.launch {
                                newTeam?.let {
                                    insertTeamIntoDatabase(it)  // This will now run in the background
                                }
                            }



                            if (nbaStats != null) {
                                val overallRecordSummary = nbaStats.record?.items?.find { it.description == "Overall Record" }?.summary ?: "N/A"
                                val homeRecordSummary = nbaStats.record?.items?.find { it.description == "Home Record" }?.summary ?: "N/A"
                                val awayRecordSummary = nbaStats.record?.items?.find { it.description == "Away Record" }?.summary ?: "N/A"
                                val standingSummary = nbaStats.standingSummary ?: "No standing summary available"

                                val stats = nbaStats.record?.items?.find { it.description == "Overall Record" }?.stats

                                val avgPointsAgainst = stats?.find { it.name == "avgPointsAgainst" }?.value?: 0.0
                                val winPercent = stats?.find { it.name == "avgPointsAgainst" }?.value?: 0.0
                                val avgPointsFor = stats?.find { it.name == "avgPointsFor" }?.value?: 0.0
                                val playoffSeed = stats?.find { it.name == "playoffSeed" }?.value?: 0
                                val streak = stats?.find { it.name == "streak" }?.value?: "No streak available"

                                val avgPointsAgainstRounded = "%.2f".format(avgPointsAgainst).toDouble()
                                val avgPointsForRounded = "%.2f".format(avgPointsFor).toDouble()

                                val nextEventName = nbaStats.nextEvent?.firstOrNull()?.shortName ?: "No upcoming events"
                                val ticketLink = nbaStats.links.find { it.relation.contains("tickets") }?.link ?: "No ticket link available"

                                teamsList.add(
                                    DisplayTeam(
                                        teamName = nbaStats.teamName,
                                        abbreviation = nbaStats.abbreviation,
                                        logoUrl = nbaStats.logos[0].logoUrl,
                                        recordSummary = overallRecordSummary,
                                        standingSummary = standingSummary,
                                        homeRecordSummary = homeRecordSummary,
                                        awayRecordSummary = awayRecordSummary,
                                        avgPointsAgainst = avgPointsAgainstRounded.toString(),
                                        avgPointsFor = avgPointsForRounded.toString(),
                                        playoffSeed = playoffSeed.toString(),
                                        nextEvent = nextEventName,      // Add next event
                                        ticketLink = ticketLink,
                                        streak = streak.toString(),
                                        color = nbaStats.color,
                                        alternateColor = nbaStats.alternateColor,
                                        winPercent = winPercent.toString()
                                    )
                                )
                                teamsList.sortBy { it.teamName }
                            }
                        } catch (e: Exception) {
                            Log.e("TeamStatsFragment", "Failed to parse JSON", e)
                        } finally {
                            checkRequestsComplete()
                        }
                    }
                })

                // Introduce a delay between requests
                //delay(200)
            }
        }
    }


    private fun checkRequestsComplete() {
        completedRequests++
        if (completedRequests == ABR_LIST.size) {
            teamAdapter.notifyDataSetChanged()
        }
    }

    private suspend fun insertTeamIntoDatabase(team: TeamEntity) {
        // Ensure the insert is on a background thread by explicitly using Dispatchers.IO
        withContext(Dispatchers.IO) {
            try {
                val teamDao = AppDatabase.getInstance(requireContext()).teamStatsDao()
                teamDao.insertAll(listOf(team))  // Insert the team into the database
            } catch (e: Exception) {
                Log.e("TeamStatsFragment", "Error inserting team into database", e)
            }
        }
    }
}