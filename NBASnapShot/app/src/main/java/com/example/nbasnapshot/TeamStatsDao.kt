package com.example.nbasnapshot

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TeamStatsDao  {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(teams: List<Team>)

    @Query("SELECT * FROM team_table")
    fun getAllTeams(): List<Team>
}