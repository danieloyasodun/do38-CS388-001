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
    val playoffSeed: Int?,
    var isFavorite: Int = 0,
    val color: String? = null,
    val alternateColor: String? = null,
    val nextEvent: String? = null,
    val ticketLink: String? = null,
    val streak: String? = null,
    val recordSummary: String,
    val standingSummary: String,
    val homeRecordSummary: String,
    val awayRecordSummary: String,
    val avgPointsAgainst: String,
    val avgPointsFor: String
)

