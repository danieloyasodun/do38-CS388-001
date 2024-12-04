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

class ProfileFragment : Fragment() {

    private lateinit var teamsAdapter: TeamAdapter
    private lateinit var playersAdapter: RosterAdapter
    private val favoriteTeams = mutableListOf<DisplayTeam>() // List of teams to display in RecyclerView
    private val favoritePlayers = mutableListOf<AthleteInfo>()
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
            favoriteTeams.addAll(teams)
            teamsAdapter.notifyDataSetChanged() // Update RecyclerView for teams
        }

        favoritesViewModel.favoritePlayers.observe(viewLifecycleOwner) { players ->
            favoritePlayers.clear()
            favoritePlayers.addAll(players)
            playersAdapter.notifyDataSetChanged()  // Update the RecyclerView
        }

        // Initialize RecyclerView and set up the adapter
        val teamsRecyclerView = view.findViewById<RecyclerView>(R.id.favoritesTeamsRecyclerView)
        teamsAdapter = TeamAdapter(requireContext(), favoriteTeams) { team ->
            // Handle long press (add the team to favorites)
            addTeamToFavorites(team)
        }
        teamsRecyclerView.adapter = teamsAdapter
        teamsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val playersRecyclerView = view.findViewById<RecyclerView>(R.id.favoritesPlayersRecyclerView)
        playersAdapter = RosterAdapter(favoritePlayers) { athlete ->
            // Handle long press (add the player to favorites)
            addPlayerToFavorites(athlete)
        }
        playersRecyclerView.adapter = playersAdapter
        playersRecyclerView.layoutManager = LinearLayoutManager(requireContext())

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

    // Function to add the team to the favorites list
    private fun addTeamToFavorites(team: DisplayTeam) {
        favoritesViewModel.addTeamToFavorites(team) // Add the team to the ViewModel

        // Show a Toast confirming that the team has been added
        Toast.makeText(requireContext(), "${team.teamName} added to favorites!", Toast.LENGTH_SHORT).show()
    }

    // Function to add the player to the favorites list
    private fun addPlayerToFavorites(athlete: AthleteInfo) {
        favoritesViewModel.addPlayerToFavorites(athlete) // Add the player to the ViewModel

        // Show a Toast confirming that the player has been added
        Toast.makeText(requireContext(), "${athlete.displayName} added to favorites!", Toast.LENGTH_SHORT).show()
    }

    // Function to update the list of favorite teams (if needed)
    fun updateFavoriteTeams(newTeams: List<DisplayTeam>) {
        favoriteTeams.clear()  // Clear the existing list
        favoriteTeams.addAll(newTeams)  // Add new teams
        teamsAdapter.notifyDataSetChanged()  // Notify the adapter that the data has changed
    }

    // Function to update the list of favorite players (if needed)
    fun updateFavoritePlayers(newPlayers: List<AthleteInfo>) {
        favoritePlayers.clear()  // Clear the existing list
        favoritePlayers.addAll(newPlayers)  // Add new players
        playersAdapter.notifyDataSetChanged()  // Notify the adapter that the data has changed
    }
}