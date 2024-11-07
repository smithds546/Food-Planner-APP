package com.example.foodplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.foodplanner.appBars.BottomAppBar
import com.example.foodplanner.appBars.TopAppBar
import com.example.foodplanner.data.Meal
import com.example.foodplanner.data.MealDao
import com.example.foodplanner.data.MealDatabase
import com.example.foodplanner.data.MealRepository
import com.example.foodplanner.data.MealViewModel
import com.example.foodplanner.meal.AddMealDialog
import com.example.foodplanner.meal.MealDetailScreen
import com.example.foodplanner.meal.MealsPage
import com.example.foodplanner.planner.SelectedMealsSection
import com.example.foodplanner.ui.theme.CWTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MealViewModel
    private lateinit var mealDatabase: MealDatabase
    private lateinit var mealDao: MealDao
    private lateinit var mealRepository: MealRepository



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the MealDatabase and MealDao
        mealDatabase = MealDatabase.getInstance(applicationContext)
        mealDao = mealDatabase.mealDao()
        mealRepository = MealRepository(mealDao)

        // Create an instance of AndroidViewModelFactory
        val viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)

        // Create an instance of MealViewModel
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(MealViewModel::class.java)



        setContent {
            val navController = rememberNavController()

                CWTheme {
                MealsPage(navController = navController)

                // Set up navigation using NavHost
                NavHost(navController, startDestination = "main") {
                    composable("main") {
                        MainContent(navController = navController, mealViewModel = viewModel)
                    }
                    composable("planner") {
                        com.example.foodplanner.planner.PlannerPage(
                            navController = navController,
                            mealViewModel = viewModel
                        )
                    }

                    composable("mealSplash") {
                        MealsPage(navController)
                    }
                    composable("NewMeal") {
                        val mealViewModel: MealViewModel = viewModel()
                        AddMealDialog(
                            navController = navController,
                            mealViewModel = mealViewModel,
                            onAddMeal = { meal ->
                                mealViewModel.insert(meal)
                                navController.popBackStack()
                            }
                        )
                    }
                    composable("MealDetailCard/{mealId}") { backStackEntry ->
                        val mealId = backStackEntry.arguments?.getString("mealId")
                        val coroutineScope = rememberCoroutineScope()

                        // Define a state variable to hold meal data
                        val meal = remember { mutableStateOf<Meal?>(null) }

                        LaunchedEffect(mealId) {
                            // Use withContext to switch to the IO dispatcher for database operations
                            val fetchedMeal = mealId?.let { id ->
                                withContext(Dispatchers.IO) {
                                    mealRepository.getMealById(id.toInt())
                                }
                            }
                            meal.value = fetchedMeal // Update the state variable with the fetched meal data
                        }

                        // Call the composable function with the fetched meal data
                        meal.value?.let { MealDetailScreen(it, onCloseClicked = {navController.popBackStack()}, viewModel = viewModel) } // Pass the meal data to the MealDetailScreen composable
                    }
                    composable("camera") {
                        CameraScreen()
                    }
                }
            }
        }
    }
}


@Composable
fun MainContent(navController: NavController, mealViewModel: MealViewModel) {
    val currentDate = remember { LocalDate.now() }
    val formattedDate = remember(currentDate) {
        DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy").format(currentDate)
    }

    Scaffold(
        topBar = {
            TopAppBar(navController, title = "Food 4 Thought")
        },
        bottomBar = {
            BottomAppBar(navController)
        },
        content = { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            bottom = 10.dp,
                            top = 10.dp,
                            start = 6.dp, //left
                            end = 6.dp  //right
                        ),
                    verticalArrangement = Arrangement.SpaceBetween // Distribute items evenly
                ) {
                    Column {
                        // Add Today's Date
                        Text(
                            text = formattedDate,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(text = "Current Meal Plan: ")
                        mealViewModel.selectedMeals.value?.let { selectedMeals ->
                            SelectedMealsSection(
                                selectedMeals = selectedMeals,
                                onMealClicked = { meal ->
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    // Add other content here (e.g., PlannerScreen, etc.)
                }
            }
        }
    )
}




@Composable
fun AccountCircleIcon(color: Color, modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Filled.AccountCircle,
        contentDescription = "Account Circle",
        tint = color,
        modifier = modifier.size(55.dp) // Adjust size as needed
    )
}
