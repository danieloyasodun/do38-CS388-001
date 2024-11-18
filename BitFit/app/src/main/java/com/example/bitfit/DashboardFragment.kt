package com.example.bitfit

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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

        observeDatabaseChanges()

        return view
    }

    private fun observeDatabaseChanges() {
        val bitFitDao = (requireActivity().application as BitFitApplication).db.bitFitDao()

        // Observe max calories
        lifecycleScope.launch {
            bitFitDao.getMaxCalories().collect { maxCalories ->
                val maxCaloriesText = maxCalories?.toString() ?: "0"
                maxCaloriesTextView.text = "Max Calories: $maxCaloriesText"
                Log.d("DashboardFragment", "Max calories updated: $maxCalories")
            }
        }

        // Observe min calories
        lifecycleScope.launch {
            bitFitDao.getMinCalories().collect { minCalories ->
                val minCaloriesText = minCalories?.toString() ?: "0"
                minCaloriesTextView.text = "Min Calories: $minCaloriesText"
                Log.d("DashboardFragment", "Min calories updated: $minCalories")
            }
        }

        // Observe average calories
        lifecycleScope.launch {
            bitFitDao.getAverage().collect { averageCalories ->
                val roundedAverage = averageCalories?.roundToInt() ?: 0
                avgCaloriesTextView.text = "Avg Calories: $roundedAverage"
                Log.d("DashboardFragment", "Average calories updated: $roundedAverage")
            }
        }
    }

    companion object {
        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }

}
