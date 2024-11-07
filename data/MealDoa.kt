package com.example.foodplanner.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(meal: Meal)

    @Delete
    suspend fun delete(movie: Meal)

    @Query("SELECT * from meals WHERE id = :id")
    fun getMealItem(id: Int): LiveData<Meal>

    @Query("SELECT * FROM meals")
    fun getAllMeals(): LiveData<List<Meal>>

    @Query("SELECT * FROM Meals WHERE id = :mealId")
    fun getMealById(mealId: Int): Meal?

    @Query("UPDATE Meals SET selected = :selected WHERE id = :mealId")
    suspend fun updateMealSelection(mealId: Int, selected: Boolean)

    @Query("SELECT * FROM Meals WHERE selected = 1")
    suspend fun getSelectedMeals(): List<Meal>

    @Update
    suspend fun updateMeal(meal: Meal)
}