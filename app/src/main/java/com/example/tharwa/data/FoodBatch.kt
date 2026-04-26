package com.example.tharwa.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_batches")
data class FoodBatch(
    @PrimaryKey val batchId: String,
    val cropType: String,
    val origin: String,
    val certification: String,
    val expirationDate: String,
    val farmerUsername: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "Pending", // "Pending", "Verified"
    val txHash: String? = null
)
