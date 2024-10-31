package com.example.bitfit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bitfit.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "MainActivity/"
private const val REQUEST_CODE_ADD_FOOD = 1

class MainActivity : AppCompatActivity() {
    private val bitFitList = mutableListOf<DisplayBitFit>()
    private lateinit var bitFitRecyclerView: RecyclerView
    private lateinit var bitFitAdapter: BitFitAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bitFitAdapter = BitFitAdapter(this, bitFitList)
        binding.BitFit.layoutManager = LinearLayoutManager(this)
        binding.BitFit.adapter = bitFitAdapter

        bitFitRecyclerView = findViewById(R.id.BitFit)


        binding.addFoodButton.setOnClickListener {
            // Start AddFoodActivity for result
            val intent = Intent(this, AddFoodActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_FOOD)

        }

        binding.clearDatabaseButton.setOnClickListener {
            clearDatabase()
        }
    }


    private fun loadData() {
        lifecycleScope.launch {
            (application as BitFitApplication).db.bitFitDao().getAll().collect { entities ->
                Log.d(TAG, "Fetched ${entities.size} food items")

                bitFitList.clear()
                val displayList = entities.map { DisplayBitFit(it.food, it.calories) }
                Log.d(TAG, "Display list: $displayList")

                bitFitAdapter.updateData(displayList)
                bitFitAdapter.notifyDataSetChanged()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_FOOD && resultCode == RESULT_OK) {
            // Refresh data after returning from AddFoodActivity
            loadData()
        }
    }

    override fun onResume() {
        super.onResume()
        loadData() // Refresh data when returning to MainActivity
    }

    private fun clearDatabase() {
        lifecycleScope.launch(Dispatchers.IO) {
            (application as BitFitApplication).db.bitFitDao().deleteAll()
            launch(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, "Database cleared!", Toast.LENGTH_SHORT).show()
                loadData() // Refresh data after clearing
            }
        }
    }
}