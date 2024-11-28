package com.example.nbasnapshot

data class DisplayGame (
    val awayTeamName: String,
    val homeTeamName: String,
    val homeTeamScore: String,
    val awayTeamScore: String,
    val awayTeamLogoUrl: String,
    val homeTeamLogoUrl: String,
    val awayTeamRecord: String,
    val homeTeamRecord: String,
    val venueName: String
)