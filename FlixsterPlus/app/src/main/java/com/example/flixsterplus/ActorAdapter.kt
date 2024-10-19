package com.example.flixsterplus

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

const val ACTOR_EXTRA = "ACTOR_EXTRA"
private const val TAG = "ActorAdapter"

class ActorAdapter(private val context: Context, private val actors: List<Actor>) :
        RecyclerView.Adapter<ActorAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_actor, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // TODO: Get the individual article and bind to holder
        val actor = actors[position]
        holder.bind(actor)
    }
    override fun getItemCount() = actors.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val mediaImageView = itemView.findViewById<ImageView>(R.id.actorImage)
        private val titleTextView = itemView.findViewById<TextView>(R.id.actorName)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val actor = actors[absoluteAdapterPosition]

            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(ACTOR_EXTRA, actor)
            context.startActivity(intent)
        }

        fun bind(actor: Actor) {
            titleTextView.text = actor.name

            Glide.with(context)
                .load(actor.profilePath)
                .into(mediaImageView)
        }

    }
}