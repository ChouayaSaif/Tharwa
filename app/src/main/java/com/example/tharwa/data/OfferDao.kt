package com.example.tharwa.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface OfferDao {
    @Query("SELECT * FROM offers WHERE farmerUsername = :username ORDER BY timestamp DESC")
    fun getOffersForFarmer(username: String): Flow<List<Offer>>

    @Query("SELECT * FROM offers WHERE buyerUsername = :username ORDER BY timestamp DESC")
    fun getOffersForBuyer(username: String): Flow<List<Offer>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOffer(offer: Offer)

    @Update
    suspend fun updateOffer(offer: Offer)
}
