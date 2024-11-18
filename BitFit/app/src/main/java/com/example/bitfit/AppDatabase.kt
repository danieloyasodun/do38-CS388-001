package com.example.bitfit

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BitFitEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bitFitDao() : BitFitDao

    companion object{

        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "bitfit_database"
            ).fallbackToDestructiveMigration()
                .build()
    }
}