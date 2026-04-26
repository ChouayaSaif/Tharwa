package com.example.tharwa.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val username: String,
    val password: String,
    val role: String, // "Normal User" or "Farmer"
    val allergies: String = "",
    val dietaryPreferences: String = "",
    val avoidedIngredients: String = "",
    // Farmer specific fields
    val farmerName: String = "",
    val location: String = "",
    val farmId: String = ""
)
