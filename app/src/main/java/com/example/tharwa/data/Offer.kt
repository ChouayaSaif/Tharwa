package com.example.tharwa.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offers")
data class Offer(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val itemId: Int,
    val itemName: String,
    val buyerUsername: String,
    val farmerUsername: String,
    val price: String,
    val status: String = "Pending", // "Pending", "Accepted", "Completed"
    val timestamp: Long = System.currentTimeMillis()
)
