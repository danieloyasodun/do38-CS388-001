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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_team_stats, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.teams)

        // Set up RecyclerView
        teamAdapter = TeamAdapter(requireContext(), teamsList)
        recyclerView.adapter = teamAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        fetchTeamData()

        return view
    }

    private var completedRequests = 0

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
                            if (nbaStats != null) {
                                val overallRecordSummary = nbaStats.record?.items?.find { it.description == "Overall Record" }?.summary ?: "N/A"
                                val homeRecordSummary = nbaStats.record?.items?.find { it.description == "Home Record" }?.summary ?: "N/A"
                                val awayRecordSummary = nbaStats.record?.items?.find { it.description == "Away Record" }?.summary ?: "N/A"
                                val standingSummary = nbaStats.standingSummary ?: "No standing summary available"

                                val stats = nbaStats.record?.items?.find { it.description == "Overall Record" }?.stats

                                val avgPointsAgainst = stats?.find { it.name == "avgPointsAgainst" }?.value?: 0.0
                                val avgPointsFor = stats?.find { it.name == "avgPointsFor" }?.value?: 0.0
                                val playoffSeed = stats?.find { it.name == "playoffSeed" }?.value?: 0

                                val avgPointsAgainstRounded = "%.2f".format(avgPointsAgainst).toDouble()
                                val avgPointsForRounded = "%.2f".format(avgPointsFor).toDouble()

                                teamsList.add(
                                    DisplayTeam(
                                        teamName = nbaStats.teamName,
                                        logoUrl = nbaStats.logos[0].logoUrl,
                                        recordSummary = overallRecordSummary,
                                        standingSummary = standingSummary,
                                        homeRecordSummary = homeRecordSummary,
                                        awayRecordSummary = awayRecordSummary,
                                        avgPointsAgainst = avgPointsAgainstRounded.toString(),
                                        avgPointsFor = avgPointsForRounded.toString(),
                                        playoffSeed = playoffSeed.toString()
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
                delay(200)
            }
        }
    }


    private fun checkRequestsComplete() {
        completedRequests++
        if (completedRequests == ABR_LIST.size) {
            teamAdapter.notifyDataSetChanged()
        }
    }
}