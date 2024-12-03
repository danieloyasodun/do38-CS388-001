package com.example.nbasnapshot

import android.support.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class GameResponse(
    @SerialName("day") val day: GameDay,
    @SerialName("events") val events: List<GameEvent>
)

@Keep
@Serializable
data class GameDay(
    @SerialName("date") val date: String
)

@Keep
@Serializable
data class GameEvent(
    @SerialName("shortName") val shortName: String,
    @SerialName("competitions") val competitions: List<Competition>,
    @SerialName("status") val status: Status
)

@Keep
@Serializable
data class Status(
    @SerialName("clock") val clock: Double?,
    @SerialName("displayClock") val displayClock: String,
    @SerialName("period") val period: Int,
    @SerialName("type") val type: StatusType
)

@Keep
@Serializable
data class StatusType(
    @SerialName("name") val name: String,
    @SerialName("state") val state: String,
    @SerialName("completed") val completed: Boolean,
    @SerialName("description") val description: String,
    @SerialName("detail") val detail: String,
    @SerialName("shortDetail") val shortDetail: String
)

@Keep
@Serializable
data class Competition(
    @SerialName("venue") val venue: Venue,
    @SerialName("competitors") val competitors: List<Competitor>
)

@Keep
@Serializable
data class Venue(
    @SerialName("fullName") val fullName: String,
    @SerialName("address") val address: Address
)

@Keep
@Serializable
data class Address(
    @SerialName("city") val city: String,
    @SerialName("state") val state: String
)

@Keep
@Serializable
data class Competitor(
    @SerialName("homeAway") val homeORAway: String,
    @SerialName("winner") val winner: Boolean? = null,
    @SerialName("team") val team: GameTeam,
    @SerialName("score") val score: String,
    @SerialName("linescores") val lineScores: List<LineScore>? = null,
    @SerialName("statistics") val statistics: List<Statistic>,
    @SerialName("leaders") val leaders: List<Leader>?,
    @SerialName("records") val record: List<GRecord>
)


@Keep
@Serializable
data class LineScore(
    @SerialName("value") val value: Int
)

@Keep
@Serializable
data class GRecord(
    @SerialName("name") val name: String,
    @SerialName("summary") val summary: String
)

@Keep
@Serializable
data class Leader(
    @SerialName("displayName") val name: String,
    @SerialName("shortDisplayName") val shortDisplayName: String,
    @SerialName("leaders") val leaders: List<LeaderDetail>
)

@Keep
@Serializable
data class LeaderDetail(
    @SerialName("displayValue") val displayValue: String,
    @SerialName("value") val value: Double,
    @SerialName("athlete") val athlete: Athlete
)

@Keep
@Serializable
data class Athlete(
    @SerialName("displayName") val fullName: String,
    @SerialName("shortName") val shortName: String,
    @SerialName("headshot") val headshot: String,
    @SerialName("jersey") val jersey: String,
    @SerialName("position") val position: Position,
)

@Keep
@Serializable
data class Position(
    @SerialName("abbreviation") val abbreviation: String
)

@Keep
@Serializable
data class Statistic(
    @SerialName("name") val name: String,
    @SerialName("abbreviation") val abbreviation: String,
    @SerialName("displayValue") val displayValue: String
)

@Keep
@Serializable
data class GameTeam(
    @SerialName("name") val teamName: String,
    @SerialName("displayName") val displayName: String,
    @SerialName("shortDisplayName") val shortDisplayName: String,
    @SerialName("abbreviation") val abbreviation: String,
    @SerialName("color") val color: String,
    @SerialName("alternateColor") val altColor: String,
    @SerialName("logo") val logo: String,
)