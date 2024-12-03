package com.example.nbasnapshot

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Call

interface ApiService {
    @GET("basketball/nba/teams/{abbr}/roster")
    fun getRoster(@Path("abbr") abbr: String): Call<RosterResponse>
}
