package com.example.bitfit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bitfit.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "MainActivity/"
private const val REQUEST_CODE_ADD_FOOD = 1

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val fragmentManager: FragmentManager = supportFragmentManager

        val foodListFragment: Fragment = FoodListFragment()
        val dashboardFragment: Fragment = DashboardFragment()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            lateinit var fragment: Fragment
            when (item.itemId) {
                R.id.nav_log -> fragment = foodListFragment
                R.id.nav_dashboard -> fragment = dashboardFragment
            }
            replaceFragment(fragment)
            true
        }

        // Add FoodListFragment to the activity if it's the first time loading
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.food_frame_layout, foodListFragment)
                .commit()
        }

        binding.addFoodButton.setOnClickListener {
            // Start AddFoodActivity for result
            val intent = Intent(this, AddFoodActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_FOOD)
        }

        binding.clearDatabaseButton.setOnClickListener {
            clearDatabase()
        }

        bottomNavigationView.selectedItemId = R.id.nav_log
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_FOOD && resultCode == RESULT_OK) {
            // Notify the fragment to refresh its data
            val fragment = supportFragmentManager.findFragmentById(R.id.food_frame_layout) as? FoodListFragment
            fragment?.loadData()
        }
    }


    private fun clearDatabase() {
        lifecycleScope.launch(Dispatchers.IO) {
            (application as BitFitApplication).db.bitFitDao().deleteAll()
            launch(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, "Database cleared!", Toast.LENGTH_SHORT).show()
                val fragment = supportFragmentManager.findFragmentById(R.id.food_frame_layout) as? FoodListFragment
                fragment?.loadData()
            }
        }
    }

    private fun replaceFragment(foodListFragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.food_frame_layout, foodListFragment)
        fragmentTransaction.commit()
    }
}