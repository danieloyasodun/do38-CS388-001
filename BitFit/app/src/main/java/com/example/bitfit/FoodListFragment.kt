package com.example.bitfit

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

private const val TAG = "FoodListFragment"

class FoodListFragment : Fragment() {
    private val food = mutableListOf<DisplayBitFit>()
    private lateinit var foodRecyclerView: RecyclerView
    private lateinit var foodAdapter: BitFitAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_food_list, container, false)

        val layoutManager = LinearLayoutManager(context)
        foodRecyclerView = view.findViewById(R.id.food_recycler_view)
        foodRecyclerView.layoutManager = layoutManager
        foodRecyclerView.setHasFixedSize(true)
        foodAdapter = BitFitAdapter(view.context, food)
        foodRecyclerView.adapter = foodAdapter

        return view
    }
    companion object {
        fun newInstance(): FoodListFragment {
            return FoodListFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
    }

    fun loadData() {
        lifecycleScope.launch {
            (requireActivity().application as BitFitApplication).db.bitFitDao().getAll().collect { entities ->
                Log.d(TAG, "Fetched ${entities.size} food items")
                val displayList = entities.map { DisplayBitFit(it.food, it.calories) }
                Log.d(TAG, "Display list: $displayList")
                foodAdapter.updateData(displayList)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadData() // Refresh data when returning to MainActivity
    }
}