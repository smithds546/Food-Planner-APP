package com.example.foodplanner.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Meals")
data class Meal(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: String,
    val food: String,
    val time: Float,
    val nutrition: String,
    val selected: Boolean = false, // New field for indicating whether the meal is selected
    val imageUri: String? = null

)
