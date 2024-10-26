package com.codepath.articlesearch

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {
    private lateinit var cacheSwitch: SwitchCompat
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        cacheSwitch = findViewById(R.id.switch_cache)

        // Load saved preference for caching
        val isCachingEnabled = sharedPreferences.getBoolean("enable_cache", false)
        cacheSwitch.isChecked = isCachingEnabled

        // Save preference when switch is toggled
        cacheSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("enable_cache", isChecked).apply()
        }
    }
}
