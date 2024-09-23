package com.example.wishlist

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.mutableListOf

class MainActivity : AppCompatActivity() {
    private val items: MutableList<Item> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val wishlistRv = findViewById<RecyclerView>(R.id.wishlistRv)

        val nameET = findViewById<EditText>(R.id.nameET)
        val priceEt = findViewById<EditText>(R.id.priceET)
        val urlET = findViewById<EditText>(R.id.urlET)
        val submitButton = findViewById<Button>(R.id.submitButton)

        val itemAdapter = ItemAdapter(items)

        wishlistRv.layoutManager = LinearLayoutManager(this)
        wishlistRv.adapter = itemAdapter

        submitButton.setOnClickListener {
            val name = nameET.text.toString()
            val price = priceEt.text.toString()
            val url = urlET.text.toString()

            if (name.isNotEmpty() && price.isNotEmpty() && url.isNotEmpty()) {
                val newItem = Item(name, price, url)
                itemAdapter.addItem(newItem)
                nameET.text.clear()
                priceEt.text.clear()
                urlET.text.clear()
            }

        }
    }
}