package com.example.bitfit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

const val BITFIT_EXTRA = "BITFIT_EXTRA"
private const val TAG = "BitFitAdapter"

class BitFitAdapter (private val context: Context, private val dIsplayBitFIt: MutableList<DisplayBitFit>) :
        RecyclerView.Adapter<BitFitAdapter.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val view = LayoutInflater.from(context).inflate(R.layout.item_bitfit, parent, false)
                return ViewHolder(view)
            }

            override fun onBindViewHolder(holder: BitFitAdapter.ViewHolder, position: Int) {
                val bitFit = dIsplayBitFIt[position]
                holder.bind(bitFit)
            }

            override fun getItemCount(): Int = dIsplayBitFIt.size

            fun updateData(bitFit: List<DisplayBitFit>) {
                dIsplayBitFIt.clear()
                dIsplayBitFIt.addAll(bitFit)
                notifyDataSetChanged()
            }

            inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                private val foodTextView = itemView.findViewById<TextView>(R.id.food_name)
                private val caloriesTextView = itemView.findViewById<TextView>(R.id.calorie_number)



                fun bind(bitFit: DisplayBitFit) {
                    foodTextView.text = bitFit.food
                    caloriesTextView.text = bitFit.calories?.toString() ?: "0"
                }

            }

        }