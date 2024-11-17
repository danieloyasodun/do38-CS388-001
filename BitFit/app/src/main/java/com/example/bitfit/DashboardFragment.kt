package com.example.bitfit

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private const val TAG = "DashboardFragment"

class DashboardFragment : Fragment() {
    private lateinit var avgCaloriesTextView: TextView
    private lateinit var minCaloriesTextView: TextView
    private lateinit var maxCaloriesTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        avgCaloriesTextView = view.findViewById(R.id.avgCaloriesTextView)
        minCaloriesTextView = view.findViewById(R.id.minCaloriesTextView)
        maxCaloriesTextView = view.findViewById(R.id.maxCaloriesTextView)

        getAverageCalories()
        getMaxCalories()
        getMinCalories()

        return view
    }

    companion object {
        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }

    private fun getMaxCalories() {
        lifecycleScope.launch(Dispatchers.IO) { // Ensure this runs on a background thread
            val maxFoodItem = (requireActivity().application as BitFitApplication).db.bitFitDao().getMax()
            launch(Dispatchers.Main) { // Switch back to the main thread to update UI
                if (maxFoodItem != null) {
                    Log.d(TAG, "Food with highest calories: ${maxFoodItem.food} with ${maxFoodItem.calories} calories")
                    maxCaloriesTextView.text = "Max Calories: ${maxFoodItem.calories}"
                } else {
                    Log.d(TAG, "No food items available.")
                    maxCaloriesTextView.text = "Max Calories: 0"
                }
            }
        }
    }

    private fun getMinCalories() {
        lifecycleScope.launch(Dispatchers.IO) { // Ensure this runs on a background thread
            val minFoodItem = (requireActivity().application as BitFitApplication).db.bitFitDao().getMin()
            launch(Dispatchers.Main) { // Switch back to the main thread to update UI
                if (minFoodItem != null) {
                    Log.d(TAG, "Food with lowest calories: ${minFoodItem.food} with ${minFoodItem.calories} calories")
                    minCaloriesTextView.text = "Min Calories: ${minFoodItem.calories}"
                } else {
                    Log.d(TAG, "No food items available.")
                    minCaloriesTextView.text = "Min Calories: 0"
                }
            }
        }
    }

    private fun getAverageCalories() {
        lifecycleScope.launch(Dispatchers.IO) { // Ensure this runs on a background thread
            val averageCalories = (requireActivity().application as BitFitApplication).db.bitFitDao().getAverage()
            launch(Dispatchers.Main) { // Switch back to the main thread to update UI
                val roundedAverage = averageCalories.roundToInt() // Round to the nearest whole number
                Log.d(TAG, "Average calories: $roundedAverage")
                avgCaloriesTextView.text = "Avg Calories: $roundedAverage"
            }
        }
    }

}