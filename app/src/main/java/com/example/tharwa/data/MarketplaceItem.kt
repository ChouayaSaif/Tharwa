package com.example.tharwa.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "marketplace_items")
data class MarketplaceItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val price: String,
    val farm: String,
    val imageEmoji: String,
    val farmerUsername: String,
    val isAvailable: Boolean = true
)
