package com.example.flixster

import com.google.gson.annotations.SerializedName

class NowPlayingMovie {
    @SerializedName("original_title")
    var title: String? = null

    @JvmField
    @SerializedName("overview")
    var overview: String? = null

    @SerializedName("poster_path")
    var movieImageUrl: String? = null
}