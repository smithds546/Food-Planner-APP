package com.example.foodplanner.data

import androidx.lifecycle.LiveData

class MealRepository(private val mealDao: MealDao) {
    val allMeals: LiveData<List<Meal>> = mealDao.getAllMeals()

    suspend fun insert(meal: Meal){
        mealDao.insert(meal)
    }

    suspend fun getMealById(mealId: Int): Meal? {
        return mealDao.getMealById(mealId)
    }


    suspend fun delete(meal: Meal) {
        mealDao.delete(meal)
    }

    // Function to select a meal
    suspend fun selectMeal(mealId: Int) {
        mealDao.updateMealSelection(mealId, selected = true)
    }

    // Function to deselect a meal
    suspend fun deselectMeal(mealId: Int) {
        mealDao.updateMealSelection(mealId, selected = false)
    }
    suspend fun getSelectedMeals(): List<Meal> {
        return mealDao.getSelectedMeals()
    }
    suspend fun updateMealWithImageUri(meal: Meal, imageUri: String?) {
        val updatedMeal = meal.copy(imageUri = imageUri)
        mealDao.updateMeal(updatedMeal)
    }

}