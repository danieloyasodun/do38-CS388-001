package com.example.nbasnapshot

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
//import com.example.nbasnapshot.databinding.ActivityMainBinding
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.Headers
import org.json.JSONException
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.serialization.decodeFromString

private const val TAG = "MainActivity/"
private  val ABR_LIST = listOf("ATL", "BOS", "BKN", "CHA", "CHI", "CLE", "DAL", "DEN", "DET", "GS",
                                "HOU", "IND", "LAC", "LAL", "MEM", "MIA", "MIL", "MIN", "NO", "NY",
                                "OKC", "ORL", "PHI", "PHX", "POR", "SAC", "SA", "TOR", "UTAH", "WSH")
private const val TEAM_API_URL = "https://site.api.espn.com/apis/site/v2/sports/basketball/nba/teams"
private const val ARTICLE_SEARCH_URL2 = "https://site.api.espn.com/apis/site/v2/sports/basketball/nba/scoreboard"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Default fragment
        if (savedInstanceState == null) {
        }

        // Set up navigation listener
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_team_stats -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, TeamStats())
                        .commit()
                    true
                }
                R.id.nav_player_stats -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, PlayerStats())
                        .commit()
                    true
                }
                R.id.nav_standings -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, Standings())
                        .commit()
                    true
                }
                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment())
                        .commit()
                    true
                }

                else -> false
            }
        }
    }
}




    /*private fun fetchAllData() {
        fetchData(ARTICLE_SEARCH_URL1)
        fetchData(ARTICLE_SEARCH_URL2)

        ABR_LIST.forEach { abr ->
            val teamRosterUrl = "https://site.api.espn.com/apis/site/v2/sports/basketball/nba/teams/$abr/roster"
            val teamDetailsUrl = "https://site.api.espn.com/apis/site/v2/sports/basketball/nba/teams/$abr"
            fetchData(teamRosterUrl)
            fetchData(teamDetailsUrl)
        }
    }

    private fun fetchData(url: String) {
        val client = AsyncHttpClient()
        client.get(url, object : JsonHttpResponseHandler() {
            override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?) {
                Log.e(TAG, "Failed to fetch data: $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON?) {
                Log.i(TAG, "Successfully fetched data")
            }
        })

    }*/