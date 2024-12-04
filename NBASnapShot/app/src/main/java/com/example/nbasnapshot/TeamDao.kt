package com.example.nbasnapshot

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
}