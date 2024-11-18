package com.example.bitfit

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BitFitDao {
    @Query("SELECT * FROM bitfit_table")
    fun getAll(): Flow<List<BitFitEntity>>

    @Insert
    fun insertAll(bitFit: List<BitFitEntity>)

    @Query("DELETE FROM bitfit_table")
    fun deleteAll()

    @Query("SELECT MAX(calories) FROM bitfit_table WHERE calories IS NOT NULL")
    fun getMaxCalories(): Flow<Int?>

    @Query("SELECT MIN(calories) FROM bitfit_table WHERE calories IS NOT NULL")
    fun getMinCalories(): Flow<Int?>

    @Query("SELECT AVG(calories) FROM bitfit_table WHERE calories IS NOT NULL")
    fun getAverage(): Flow<Double?>
}