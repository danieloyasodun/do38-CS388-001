package com.example.nbasnapshot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import com.example.nbasnapshot.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


private val EAST_ABR = listOf("ATL", "BOS", "BKN", "CHA", "CHI", "CLE", "DET", "IND", "MIA", "MIL",
                                "NY", "ORL", "PHI", "TOR", "WSH")
private val WEST_ABR = listOf("DAL", "DEN", "GS", "HOU", "LAC", "LAL", "MEM", "MIN", "NO", "OKC",
                                "PHX", "POR", "SAC", "SA", "UTAH")
class Standings : Fragment() {
    private lateinit var eastStandingsAdapter: StandingsAdapter
    private lateinit var westStandingsAdapter: StandingsAdapter
    private val eastStandings = mutableListOf<TeamEntity>()
    private val westStandings = mutableListOf<TeamEntity>()
    private lateinit var database: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_standings, container, false)
        val recyclerViewEast = view.findViewById<RecyclerView>(R.id.recyclerViewEast)
        val recyclerViewWest = view.findViewById<RecyclerView>(R.id.recyclerViewWest)

        database = AppDatabase.getInstance(requireContext())

        eastStandingsAdapter = StandingsAdapter(eastStandings)
        westStandingsAdapter = StandingsAdapter(westStandings)

        recyclerViewEast.adapter = eastStandingsAdapter
        recyclerViewEast.layoutManager = LinearLayoutManager(requireContext())

        recyclerViewWest.adapter = westStandingsAdapter
        recyclerViewWest.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            fetchTeamData()
        }

        return view
    }

    private suspend fun fetchTeamData() {
        // For demonstration, we assume you've fetched all teams and populated the list `teamsList`
        val teamsList: List<TeamEntity> = getTeamsFromDatabaseOrApi()

        // Split teams into East and West conferences
        eastStandings.clear()
        westStandings.clear()

        teamsList.forEach { team ->
            if (EAST_ABR.contains(team.abbreviation)) {
                eastStandings.add(team)
            } else if (WEST_ABR.contains(team.abbreviation)) {
                westStandings.add(team)
            }
        }

        // Sort each conference list by win percentage
        eastStandings.sortByDescending { it.winPercentage.toDouble() }
        westStandings.sortByDescending { it.winPercentage.toDouble() }

        // Notify the adapter that data has been updated
        eastStandingsAdapter.notifyDataSetChanged()
        westStandingsAdapter.notifyDataSetChanged()
    }

    private suspend fun getTeamsFromDatabaseOrApi(): List<TeamEntity> {
        return withContext(Dispatchers.IO) {
            // Ensure database query is run on background thread
            database.teamStatsDao().getAllTeams()
        }
    }
}