package com.example.tharwa.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MarketplaceItemDao {
    @Query("SELECT * FROM marketplace_items WHERE isAvailable = 1")
    fun getAllAvailableItems(): Flow<List<MarketplaceItem>>

    @Query("SELECT * FROM marketplace_items WHERE farmerUsername = :username")
    fun getItemsByFarmer(username: String): Flow<List<MarketplaceItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: MarketplaceItem)

    @Update
    suspend fun updateItem(item: MarketplaceItem)
}
