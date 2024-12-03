package com.example.nbasnapshot

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Locale

class RosterAdapter(private val athletes: List<AthleteInfo>) :
    RecyclerView.Adapter<RosterAdapter.RosterViewHolder>(){
        inner class RosterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val athleteImageView: ImageView = itemView.findViewById(R.id.playerImage)
            private val athleteNameTextView: TextView = itemView.findViewById(R.id.playerName)
            private val athleteHeightWeightTextView: TextView = itemView.findViewById(R.id.playerHeightWeight)
            private val athleteAgeTextView: TextView = itemView.findViewById(R.id.playerAge)
            private val athleteNumberTextView: TextView = itemView.findViewById(R.id.playerNumber)
            private val athleteCollegeTextView: TextView = itemView.findViewById(R.id.playerCollege)

            fun bind(athlete: AthleteInfo) {
                val formattedDate = athlete.dateOfBirth.let { dob ->
                    try {
                        // Parse the date string into a Date object
                        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.getDefault())
                        val date = inputFormat.parse(dob)

                        // Format the Date object into the desired format
                        val outputFormat = SimpleDateFormat("M/d/yyyy", Locale.getDefault())
                        if (date != null) {
                            outputFormat.format(date)
                        } else {
                            Log.e("RosterAdapter", "Failed to parse date: $dob")
                        }
                    } catch (e: Exception) {
                        // Handle any parsing errors (return the original string if parsing fails)
                        dob
                    }
                }
                athleteNameTextView.text = athlete.displayName
                athleteHeightWeightTextView.text = "${athlete.displayHeight}, ${athlete.displayWeight}"
                athleteAgeTextView.text = "$formattedDate, (${athlete.age})"
                athleteNumberTextView.text = "#${athlete.jersey}, ${athlete.position.abbreviation}"
                athleteCollegeTextView.text = athlete.college?.name ?: "International"

                Glide.with(itemView.context)
                    .load(athlete.headshot.href)
                    .into(athleteImageView)

            }

        }

    override fun onBindViewHolder(holder: RosterViewHolder, position: Int) {
        val athlete = athletes[position]
        holder.bind(athlete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RosterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_roster, parent, false)
        return RosterViewHolder(view)
    }

    override fun getItemCount() = athletes.size

}