package com.example.tharwa.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodBatchDao {
    @Query("SELECT * FROM food_batches WHERE farmerUsername = :username ORDER BY timestamp DESC")
    fun getBatchesForFarmer(username: String): Flow<List<FoodBatch>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBatch(batch: FoodBatch)

    @Query("SELECT * FROM food_batches WHERE batchId = :id LIMIT 1")
    suspend fun getBatchById(id: String): FoodBatch?
}
