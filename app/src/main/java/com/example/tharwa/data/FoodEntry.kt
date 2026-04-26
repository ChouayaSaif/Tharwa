package com.example.tharwa.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_entries")
data class FoodEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val foodName: String,
    val status: String, // "Safe", "Risky", "Expired"
    val reason: String,
    val recommendation: String,
    val timestamp: Long = System.currentTimeMillis()
)
