package com.example.bitfit

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "AddFoodActivity/"

class AddFoodActivity : AppCompatActivity() {
    private lateinit var foodEditText: EditText
    private lateinit var calorieEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)

        foodEditText = findViewById(R.id.foods_edit_text)
        calorieEditText = findViewById(R.id.calories_edit_text)

        val addFoodButton = findViewById<Button>(R.id.addFoodButton)

        addFoodButton.setOnClickListener {
            saveFood()
        }
    }

    private fun saveFood() {
        val foodName = foodEditText.text.toString()
        val caloriesInput= calorieEditText.text.toString()

        val calories = caloriesInput.toIntOrNull()

        if (foodName.isNotEmpty() && calories != null) {
            val foodEntry = BitFitEntity(food = foodName, calories = calories)

            // Call the addFoodItem function here
            addFoodItem(foodEntry)

        } else {
            Toast.makeText(this, "Food name and calories are required", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addFoodItem(food: BitFitEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            (application as BitFitApplication).db.bitFitDao().insertAll(listOf(food))

            // Notify the result back to MainActivity
            setResult(RESULT_OK)
            launch(Dispatchers.Main) {
                Toast.makeText(this@AddFoodActivity, "Food added!", Toast.LENGTH_SHORT).show()
                finish() // Closes the activity after adding the food item
            }
        }
    }


}