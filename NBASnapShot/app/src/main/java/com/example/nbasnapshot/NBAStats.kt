package com.example.nbasnapshot

import android.support.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class NBAStats(
    @SerialName("displayName") val teamName: String,
    @SerialName("abbreviation") val abbreviation: String,
    @SerialName("name") val name: String,
    @SerialName("logos") val logos: List<Logo>,
    @SerialName("links") val links: List<TeamLink>,
    @SerialName("record") val record: Record?,
    @SerialName("standingSummary") val standingSummary: String? = null,
    @SerialName("nextEvent") val nextEvent: List<NextEvent>? = null,
    @SerialName("color") val color: String,
    @SerialName("alternateColor") val alternateColor: String
)

@Keep
@Serializable
data class NextEvent(
    @SerialName("shortName") val shortName: String,
)


@Keep
@Serializable
data class Logo(
    @SerialName("href") val logoUrl: String // URL for the team's logo image
)

@Keep
@Serializable
data class TeamLink(
    @SerialName("rel") val relation: List<String>, // List of relations (e.g., ["team", "basketball", "nba"])
    @SerialName("href") val link: String // URL link for the team (e.g., "/nba/team/_/id/1")
)

@Keep
@Serializable
data class Record(
    @SerialName("items") val items: List<RecordItem>?
)

@Keep
@Serializable
data class RecordItem(
    @SerialName("description") val description: String, // Description of the record (e.g., "Overall Record")
    @SerialName("summary") val summary: String, // The record summary (e.g., "7-10")
    @SerialName("stats") val stats: List<Stat>?
)

@Keep
@Serializable
data class TeamResponse(
    @SerialName("team") val team: NBAStats? = null // The team data
)

@Keep
@Serializable
data class Stat(
    @SerialName("name") val name: String, // Name of the stat (e.g., "W")
    @SerialName("value") val value: Double // Value of the stat (e.g., "7")
)