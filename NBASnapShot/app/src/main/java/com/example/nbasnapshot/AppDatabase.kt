package com.example.nbasnapshot

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [TeamEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun teamStatsDao(): TeamDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "NBA-Standings-db"
            )
                .addMigrations(MIGRATION_1_2) // Add the migration here
                .build()

        // Example migration from version 1 to version 2
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Example of adding a new column to the Team table
                // Adjust the SQL as needed for your actual schema change
                database.execSQL("ALTER TABLE team ADD COLUMN new_column INTEGER DEFAULT 0 NOT NULL")
            }
        }
    }
}

