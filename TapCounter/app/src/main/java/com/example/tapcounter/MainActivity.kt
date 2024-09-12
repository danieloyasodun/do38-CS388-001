package com.example.tapcounter

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        var counter = 0
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            val button = findViewById<Button>(R.id.b1)
            val textView = findViewById<TextView>(R.id.textView)
            val upgradeButton = findViewById<Button>(R.id.upgradeBtn)
            button.setOnClickListener{v ->
                counter++
                textView.text = counter.toString()

                if (counter >= 100) {
                    upgradeButton.visibility = View.VISIBLE
                    upgradeButton.setOnClickListener {
                        button.setOnClickListener {
                            counter += 2
                            textView.text = counter.toString()
                        }

                        upgradeButton.visibility = View.INVISIBLE
                    }
                }

        }
            insets
        }
    }
}