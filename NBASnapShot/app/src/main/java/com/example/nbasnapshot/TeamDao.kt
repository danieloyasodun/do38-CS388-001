package com.example.nbasnapshot

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TeamDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(teams: List<TeamEntity>)

    @Query("SELECT * FROM teams")
    fun getAllTeams(): List<TeamEntity>

    @Query("DELETE FROM teams")
    fun deleteAll()

    // Insert or update a single team
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTeam(team: TeamEntity)

    // Get only favorite teams
    @Query("SELECT * FROM teams WHERE isFavorite = 1")
    fun getFavoriteTeams(): LiveData<List<TeamEntity>>
}