package com.example.nbasnapshot

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "nba_database"
    ).build()

    val favoriteTeams: LiveData<List<TeamEntity>> = db.teamStatsDao().getFavoriteTeams()

    fun addTeamToFavorites(team: DisplayTeam) {
        viewModelScope.launch {
            val teamEntity = TeamEntity(
                abbreviation = team.abbreviation,
                name = team.teamName,
                logoUrl = team.logoUrl,
                record = team.recordSummary,
                winPercentage = team.winPercent,
                playoffSeed = team.playoffSeed.toIntOrNull(),  // Assuming it's a string that can be converted
                recordSummary = team.recordSummary,
                standingSummary = team.standingSummary,
                homeRecordSummary = team.homeRecordSummary,
                awayRecordSummary = team.awayRecordSummary,
                avgPointsAgainst = team.avgPointsAgainst,
                avgPointsFor = team.avgPointsFor,
                nextEvent = team.nextEvent,
                ticketLink = team.ticketLink,
                streak = team.streak,
                color = team.color,
                alternateColor = team.alternateColor,
                isFavorite = 1
            )
            db.teamStatsDao().insertTeam(teamEntity)
        }
    }

    fun removeTeamFromFavorites(team: TeamEntity) {
        viewModelScope.launch(Dispatchers.IO) { // Use IO dispatcher for database operations
            team.isFavorite = 0  // Assuming 'isFavorite' is a boolean or integer field in your TeamEntity
            db.teamStatsDao().insertTeam(team)  // Insert or update the team in the database
        }
    }

    fun updateFavoriteStatus(team: TeamEntity, isFavorited: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedTeam = team.copy(isFavorite = isFavorited)
            db.teamStatsDao().insertTeam(updatedTeam) // Use insert to update (OnConflictStrategy.REPLACE)
        }
    }
}

