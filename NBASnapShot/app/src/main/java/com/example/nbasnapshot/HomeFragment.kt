package com.example.nbasnapshot

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Headers

private const val SCOREBOARD_API_URL = "https://site.api.espn.com/apis/site/v2/sports/basketball/nba/scoreboard"

class HomeFragment : Fragment() {
    private lateinit var gameAdapter: GameAdapter
    private val gamesList = mutableListOf<DisplayGame>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.games)

        // Set up RecyclerView
        gameAdapter = GameAdapter(requireContext(), gamesList)
        recyclerView.adapter = gameAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }
}