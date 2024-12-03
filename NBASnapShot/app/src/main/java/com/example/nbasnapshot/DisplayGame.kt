package com.example.nbasnapshot
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class DisplayGame (
    val awayTeamName: String,
    val homeTeamName: String,
    val homeTeamScore: String,
    val awayTeamScore: String,
    val awayTeamLogoUrl: String,
    val homeTeamLogoUrl: String,
    val awayTeamRecord: String,
    val homeTeamRecord: String,
    val venueName: String,
    val status: String,
    val displayClock: String, // Add display clock
    val period: Int, // Add period
    val lineScores: List<LineScores>, // Add line scores
    val leaders: List<Leaders>,
    val shortName: String
)

@Parcelize
data class LineScores(
    val period: Int,
    val awayScore: Int,
    val homeScore: Int
) : Parcelable

@Parcelize
data class Leaders(
    val displayName: String,
    val name: String,
    val value: Double,
    val imageUrl: String
) : Parcelable
