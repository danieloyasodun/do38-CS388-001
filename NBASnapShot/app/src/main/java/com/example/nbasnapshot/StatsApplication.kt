package com.example.nbasnapshot

import android.app.Application

class StatsApplication : Application() {
    val db by lazy { AppDatabase.getInstance(this) }
}
