package com.example.nbasnapshot

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "team_table")  // Specify the table name if it's not the default (class name)
data class Team(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val teamName: String,
    val abbreviation: String,
    val logoUrl: String
)

