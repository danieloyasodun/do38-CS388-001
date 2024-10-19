package com.example.flixsterplus
import android.support.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class SearchActorResponse(
    @SerialName("results")
    val results: List<Actor>?
)

@Keep
@Serializable
data class Actor(
    @SerialName("name")
    val name: String? = null,
    @SerialName("profile_path")
    val profilePath: String? = null,
    @SerialName("known_for")
    val knownFor: List<KnownFor>? = null
) : java.io.Serializable {
    val profileImageUrl = "https://image.tmdb.org/t/p/w500${profilePath}"
}

@Keep
@Serializable
data class KnownFor(
    @SerialName("title")
    val title: String? = null,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("poster_path")
    val posterPath: String? = null
) : java.io.Serializable {
    val posterImageUrl = "https://image.tmdb.org/t/p/w500${posterPath}"
}

