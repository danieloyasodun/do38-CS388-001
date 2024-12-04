package com.example.nbasnapshot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavoritesViewModel : ViewModel() {
    private val _favoriteTeams = MutableLiveData<MutableList<DisplayTeam>>()
    val favoriteTeams: LiveData<MutableList<DisplayTeam>> = _favoriteTeams

    private val _favoritePlayers = MutableLiveData<MutableList<AthleteInfo>>()
    val favoritePlayers: LiveData<MutableList<AthleteInfo>> = _favoritePlayers

    init {
        _favoriteTeams.value = mutableListOf() // Initialize the list of favorite teams
        _favoritePlayers.value = mutableListOf() // Initialize the list of favorite players
    }

    // Add a team to favorites
    fun addTeamToFavorites(team: DisplayTeam) {
        _favoriteTeams.value?.let {
            if (!it.contains(team)) {
                it.add(team)
                _favoriteTeams.value = it // Update LiveData
            }
        }
    }

    fun addPlayerToFavorites(player: AthleteInfo) {
        val currentList = _favoritePlayers.value?.toMutableList() ?: mutableListOf()
        currentList.add(player)
        _favoritePlayers.value = currentList  // Trigger the LiveData observer
    }

    // Remove a team from favorites
    fun removeTeamFromFavorites(team: DisplayTeam) {
        _favoriteTeams.value?.let {
            it.remove(team)
            _favoriteTeams.value = it // Update LiveData
        }
    }

    // Remove a player from favorites
    fun removePlayerFromFavorites(player: AthleteInfo) {
        _favoritePlayers.value?.let {
            it.remove(player)
            _favoritePlayers.value = it // Update LiveData
        }
    }
}
