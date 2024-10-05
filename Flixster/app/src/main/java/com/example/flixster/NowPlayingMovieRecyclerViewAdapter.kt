package com.example.flixster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flixster.R.id

class NowPlayingMovieRecyclerViewAdapter(
    private val movies: List<NowPlayingMovie>,
    private val mListener: OnListFragmentInteractionListener?
    )
    : RecyclerView.Adapter<NowPlayingMovieRecyclerViewAdapter.MovieViewHolder>()
    {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_now_playing_movie, parent, false)
        return MovieViewHolder(view)
    }

    inner class MovieViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var mItem: NowPlayingMovie? = null
        var mMovieTitle: TextView = mView.findViewById<View>(id.movie_title) as TextView
        var mMovieOverview: TextView = mView.findViewById<View>(id.movie_overview) as TextView
        var mMovieImage: ImageView = mView.findViewById<View>(id.movie_image) as ImageView

    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]

        holder.mItem = movie
        holder.mMovieTitle.text = movie.title
        holder.mMovieOverview.text = movie.overview
        val baseUrl = "https://image.tmdb.org/t/p/w500/"
        val fullImageUrl = baseUrl + movie.movieImageUrl
        Glide.with(holder.mView)
            .load(fullImageUrl)
            .centerInside()
            .into(holder.mMovieImage)

        holder.mView.setOnClickListener {
            holder.mItem?.let { movie ->
                mListener?.onItemClick(movie)
            }
        }
    }

        override fun getItemCount(): Int {
            return movies.size
        }
    }