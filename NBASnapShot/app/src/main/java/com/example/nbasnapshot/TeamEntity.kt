package com.example.nbasnapshot

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teams")
data class TeamEntity(
    @PrimaryKey val abbreviation: String,
    val name: String,
    val logoUrl: String,
    val record: String,
    val winPercentage: String,
    val playoffSeed: Int?
)

