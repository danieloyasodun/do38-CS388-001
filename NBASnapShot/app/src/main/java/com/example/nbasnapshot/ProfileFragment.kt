package com.example.nbasnapshot

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {

    private lateinit var teamsAdapter: TeamAdapter
    private val favoriteTeams = mutableListOf<DisplayTeam>() // List of teams to display in RecyclerView
    private val favoritesViewModel: FavoritesViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for the fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Observe the favorite teams list
        favoritesViewModel.favoriteTeams.observe(viewLifecycleOwner) { teams ->
            favoriteTeams.clear()
            favoriteTeams.addAll(teams.map { convertToDisplayTeam(it) })
            teamsAdapter.notifyDataSetChanged()
        }


        // Initialize RecyclerView and set up the adapter
        val teamsRecyclerView = view.findViewById<RecyclerView>(R.id.favoritesTeamsRecyclerView)
        teamsAdapter = TeamAdapter(requireContext(), favoriteTeams,
            onTeamLongPress = { team -> removeTeamFromFavorites(team) } // Long press to remove team
        )
        teamsRecyclerView.adapter = teamsAdapter
        teamsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Set up the logout button
        val logoutButton = view.findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            // Sign out the user from Firebase
            FirebaseAuth.getInstance().signOut()

            // Redirect to the LoginActivity
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        return view
    }

    private fun removeTeamFromFavorites(team: DisplayTeam) {
        if (favoriteTeams.contains(team)) {
            favoriteTeams.remove(team) // Remove team from the list

            // Convert DisplayTeam to TeamEntity
            val teamEntity = convertToTeamEntity(team)

            // Pass the TeamEntity to the ViewModel to remove from the database
            favoritesViewModel.removeTeamFromFavorites(teamEntity)

            Toast.makeText(requireContext(), "${team.teamName} removed from followed!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "${team.teamName} is not in followed.", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to update the list of favorite teams (if needed)
    fun updateFavoriteTeams(newTeams: List<DisplayTeam>) {
        favoriteTeams.clear()  // Clear the existing list
        favoriteTeams.addAll(newTeams)  // Add new teams
        teamsAdapter.notifyDataSetChanged()  // Notify the adapter that the data has changed
    }

    private fun convertToTeamEntity(displayTeam: DisplayTeam): TeamEntity {
        return TeamEntity(
            name = displayTeam.teamName,
            abbreviation = displayTeam.abbreviation,
            logoUrl = displayTeam.logoUrl,
            record = displayTeam.recordSummary,
            standingSummary = displayTeam.standingSummary,
            homeRecordSummary = displayTeam.homeRecordSummary,
            awayRecordSummary = displayTeam.awayRecordSummary,
            avgPointsAgainst = displayTeam.avgPointsAgainst,
            avgPointsFor = displayTeam.avgPointsFor,
            playoffSeed = displayTeam.playoffSeed?.toIntOrNull(),
            nextEvent = displayTeam.nextEvent,
            ticketLink = displayTeam.ticketLink,
            streak = displayTeam.streak,
            color = displayTeam.color,
            alternateColor = displayTeam.alternateColor,
            winPercentage = displayTeam.winPercent,
            isFavorite = 0,
            recordSummary = displayTeam.recordSummary // Set isFavorite to 0 since we're removing it from favorites
        )
    }


    // Convert TeamEntity to DisplayTeam
    private fun convertToDisplayTeam(teamEntity: TeamEntity): DisplayTeam {
        return DisplayTeam(
            teamName = teamEntity.name,
            abbreviation = teamEntity.abbreviation,
            logoUrl = teamEntity.logoUrl,  // Assuming logoUrl is part of TeamEntity
            recordSummary = teamEntity.record,  // Assuming record is part of TeamEntity
            standingSummary = teamEntity.standingSummary,  // Assuming standingSummary is part of TeamEntity,  // You might need to compute or pass this from another field
            homeRecordSummary = teamEntity.homeRecordSummary,  // Similarly, you may need to set this
            awayRecordSummary = teamEntity.awayRecordSummary,
            avgPointsAgainst = teamEntity.avgPointsAgainst,  // If applicable, get this data
            avgPointsFor = teamEntity.avgPointsFor,
            playoffSeed = teamEntity.playoffSeed?.toString() ?: "0",  // Handle nulls for playoffSeed
            nextEvent = teamEntity.nextEvent ?: "",  // Set appropriately
            ticketLink = teamEntity.ticketLink ?:"",  // Set appropriately
            streak = teamEntity.streak ?: "",  // Set appropriately
            color = teamEntity.color ?: "",  // Handle if necessary
            alternateColor = teamEntity.alternateColor ?: "",
            winPercent = teamEntity.winPercentage  // Assuming winPercentage is part of TeamEntity
        )
    }
}