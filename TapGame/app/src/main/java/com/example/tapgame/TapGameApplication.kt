package com.example.tapgame

import android.app.Application
import com.example.tapgame.database.AppDatabase

class TapGameApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}