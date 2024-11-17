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

    @Query("SELECT * FROM bitfit_table ORDER BY calories DESC LIMIT 1")
    fun getMax(): BitFitEntity?

    @Query("SELECT * FROM bitfit_table ORDER BY calories ASC LIMIT 1")
    fun getMin(): BitFitEntity?

    @Query("SELECT AVG(calories) FROM bitfit_table")
    fun getAverage(): Double
}