package com.example.tharwa.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodEntryDao {
    @Query("SELECT * FROM food_entries WHERE username = :username ORDER BY timestamp DESC")
    fun getHistoryForUser(username: String): Flow<List<FoodEntry>>

    @Insert
    suspend fun insertFoodEntry(entry: FoodEntry)

    @Query("SELECT * FROM food_entries ORDER BY timestamp DESC")
    fun getAllEntries(): Flow<List<FoodEntry>>
}
