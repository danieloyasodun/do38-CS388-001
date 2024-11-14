package com.example.firebasetodoapp

import android.app.Application
import com.example.firebasetodoapp.database.AppDatabase

class ToDoApplication : Application() {
    val db by lazy { AppDatabase.getInstance(this) }
}