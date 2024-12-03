package com.example.nbasnapshot

import android.support.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class RosterResponse(
    @SerialName("athletes") val athletes: List<AthleteInfo>
)

@Keep
@Serializable
data class AthleteInfo(
    @SerialName("displayName") val displayName: String,
    @SerialName("displayWeight") val displayWeight: String,
    @SerialName("displayHeight") val displayHeight: String,
    @SerialName("age") val age: Int,
    @SerialName("dateOfBirth") val dateOfBirth: String,
    @SerialName("debutYear") val debutYear: Int,
    @SerialName("college") val college: College?,
    @SerialName("headshot") val headshot: Headshot,
    @SerialName("jersey") val jersey: String,
    @SerialName("position") val position: PlayerPosition,
)

@Keep
@Serializable
data class College(
    @SerialName("name") val name: String,
    @SerialName("shortName") val shortName: String,
)

@Keep
@Serializable
data class Headshot(
    @SerialName("href") val href: String
)

@Keep
@Serializable
data class PlayerPosition(
    @SerialName("displayName") val displayName: String,
    @SerialName("abbreviation") val abbreviation: String
)