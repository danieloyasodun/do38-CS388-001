package com.example.flixsterplus

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

private const val TAG = "DetailActivity"

class DetailActivity : AppCompatActivity() {
    private lateinit var  actorImageView: ImageView
    private lateinit var  actorMovieImageView: ImageView
    private lateinit var  actorNameTextView: TextView
    private lateinit var  actorMovieTitleView: TextView
    private lateinit var  actorMovieOverview: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        actorImageView = findViewById(R.id.actorImage)
        actorMovieImageView = findViewById(R.id.actorMovieImage)
        actorNameTextView = findViewById(R.id.actorDetailName)
        actorMovieTitleView = findViewById(R.id.actorMovieTitle)
        actorMovieOverview = findViewById(R.id.actorMovieOverview)

        val actor = intent.getSerializableExtra(ACTOR_EXTRA) as Actor

        actorNameTextView.text = actor.name
        actorMovieTitleView.text = actor.knownFor?.get(0)?.title
        actorMovieOverview.text = actor.knownFor?.get(0)?.overview

        val baseUrl = "https://image.tmdb.org/t/p/w500"
        val actorImageUrl = baseUrl + actor.profilePath

        Glide.with(this)
            .load(actorImageUrl)
            .into(actorImageView)

        val movieImageUrl = baseUrl + actor.knownFor?.get(0)?.posterPath

        Glide.with(this)
            .load(movieImageUrl)
            .into(actorMovieImageView)
    }
}