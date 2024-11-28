package com.example.nbasnapshot

data class DisplayTeam(
    val teamName: String,
    val logoUrl: String,
    val recordSummary: String,
    val standingSummary: String,
    val homeRecordSummary: String,
    val awayRecordSummary: String,
    val avgPointsAgainst: String,
    val avgPointsFor: String,
    val playoffSeed: String
) : java.io.Serializable