package com.example.foodplanner.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MealViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MealRepository
    val allMeals: LiveData<List<Meal>>

    private val _selectedMeals = MutableLiveData<List<Meal>>()
    val selectedMeals: LiveData<List<Meal>> = _selectedMeals


    init {
        val mealDao = MealDatabase.getInstance(application).mealDao()
        repository = MealRepository(mealDao)
        allMeals = repository.allMeals

        // Initialize selected meals LiveData with current selected meals
        viewModelScope.launch {
            _selectedMeals.value = repository.getSelectedMeals()
        }
    }

    fun insert(meal: Meal) = viewModelScope.launch {
        repository.insert(meal)
    }

    fun delete(meal: Meal) = viewModelScope.launch {
        repository.delete(meal)
    }

    // Function to select a meal
    fun selectMeal(mealId: Int) {
        viewModelScope.launch {
            // Update selected meals in the repository
            repository.selectMeal(mealId)
            // Update selected meals LiveData with current selected meals
            _selectedMeals.value = repository.getSelectedMeals()
        }
    }

    // Function to deselect a meal
    fun deselectMeal(mealId: Int) {
        viewModelScope.launch {
            // Update selected meals in the repository
            repository.deselectMeal(mealId)
            // Update selected meals LiveData with current selected meals
            _selectedMeals.value = repository.getSelectedMeals()
        }
    }

    fun updateMealWithImageUri(meal: Meal, imageUri: String?) {
        viewModelScope.launch {
            repository.updateMealWithImageUri(meal, imageUri)
        }
    }
}
