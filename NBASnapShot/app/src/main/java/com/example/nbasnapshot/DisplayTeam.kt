package com.example.nbasnapshot

data class DisplayTeam(
    val teamName: String,
    val abbreviation: String,
    val logoUrl: String,
    val recordSummary: String,
    val standingSummary: String,
    val homeRecordSummary: String,
    val awayRecordSummary: String,
    val avgPointsAgainst: String,
    val avgPointsFor: String,
    val playoffSeed: String,
    val nextEvent: String,
    val ticketLink: String,
    val streak: String,
    val color : String,
    val alternateColor : String,
    val winPercent : String
) : java.io.Serializable