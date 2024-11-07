package com.example.foodplanner.planner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foodplanner.appBars.BottomAppBar
import com.example.foodplanner.appBars.TopAppBar
import com.example.foodplanner.data.Meal
import com.example.foodplanner.data.MealViewModel
import com.example.foodplanner.ui.theme.Catchup


@Composable
fun PlannerPage(navController: NavController, mealViewModel: MealViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(navController, title = "Planner")
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
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(text = "Planning for ")
                    Spacer(modifier = Modifier.height(6.dp))
                    PlannerScreen(navController, mealViewModel)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    )
}


@Composable
fun PlannerScreen(navController: NavController, mealViewModel: MealViewModel) {
    // Retrieve all meals from the view model
    val allMeals by mealViewModel.allMeals.observeAsState(initial = emptyList())

    // Filter meals by meal type
    val breakfastMeals = allMeals.filter { it.type == "Breakfast" }
    val lunchMeals = allMeals.filter { it.type == "Lunch" }
    val dinnerMeals = allMeals.filter { it.type == "Dinner" }
    val snackMeals = allMeals.filter { it.type == "Snack" }

    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp) // 12.dp spacing between items
    ) {
        item {
            // First row with two meal type tiles
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MealTypeTile(
                    mealType = "Breakfast",
                    meals = breakfastMeals,
                    mealViewModel = mealViewModel,
                    onMealSelectionChanged = { /* No need to handle this callback */ },
                    modifier = Modifier.weight(1f)
                )
                MealTypeTile(
                    mealType = "Lunch",
                    meals = lunchMeals,
                    mealViewModel = mealViewModel,
                    onMealSelectionChanged = { /* No need to handle this callback */ },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            // Second row with two meal type tiles
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MealTypeTile(
                    mealType = "Dinner",
                    meals = dinnerMeals,
                    mealViewModel = mealViewModel,
                    onMealSelectionChanged = { /* No need to handle this callback */ },
                    modifier = Modifier.weight(1f)
                )
                MealTypeTile(
                    mealType = "Snack",
                    meals = snackMeals,
                    mealViewModel = mealViewModel,
                    onMealSelectionChanged = { /* No need to handle this callback */ },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            mealViewModel.selectedMeals.value?.let { selectedMeals ->
                SelectedMealsSection(
                    selectedMeals = selectedMeals,
                    onMealClicked = { meal ->
                        // Handle meal click event if needed
                    }
                )
            }
        }
    }
}


@Composable
fun MealTypeTile(
    mealType: String,
    meals: List<Meal>,
    mealViewModel: MealViewModel,
    onMealSelectionChanged: () -> Unit,
    modifier: Modifier = Modifier
) {
    val showDialogState = remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = mealType,
            fontSize = 20.sp,
            // Add styling as needed
        )
        Button(
            onClick = { showDialogState.value = true }
        ) {
            Text(text = "Select Meals")
        }

        // Show dialog for selecting meals
        if (showDialogState.value) {
            MealTypeDialog(
                mealType = mealType,
                meals = meals,
                mealViewModel = mealViewModel,
                onDismissRequest = { showDialogState.value = false },
                onMealSelectionChanged = {
                    onMealSelectionChanged()
                    showDialogState.value = false // Close dialog after meal selection
                }
            )
        }
    }
}


@Composable
fun SelectedMealsSection(
    selectedMeals: List<Meal>,
    onMealClicked: (Meal) -> Unit = {}
) {
    if (selectedMeals.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Selected Meals:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                //color = MaterialTheme.colors.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            selectedMeals.forEach { selectedMeal ->
                var isStrikethrough by remember { mutableStateOf(false) }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .clickable {
                            isStrikethrough = !isStrikethrough
                            onMealClicked(selectedMeal)
                        },
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = selectedMeal.food,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textDecoration = if (isStrikethrough) TextDecoration.LineThrough else TextDecoration.None
                            )
                        )
                        // Add any additional meal details or actions here
                    }
                }
            }
        }
    } else {
        Text(text = "Please select your meals for today.")
    }
}






@Composable
fun MealTypeDialog(
    mealType: String,
    meals: List<Meal>,
    mealViewModel: MealViewModel,
    onDismissRequest: () -> Unit,
    onMealSelectionChanged: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = mealType) },
        text = {
            Column {
                Text(text = "Please select food for: $mealType")
                Divider()

                meals.forEachIndexed { index, meal ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = meal.selected,
                            onCheckedChange = { isChecked ->
                                if (isChecked) {
                                    mealViewModel.selectMeal(meal.id)
                                } else {
                                    mealViewModel.deselectMeal(meal.id)
                                }
                            }
                        )
                        Text(
                            text = meal.food,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onMealSelectionChanged()
                    onDismissRequest()
                }
            ) {
                Text(text = "OK")
            }
        }
    )
}







@Composable
fun ViewPlan(navController: NavController) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 75.dp),
        color = Catchup,
        shape = RoundedCornerShape(25.dp),
    ) {
        Box(
            modifier = Modifier
                .clickable {navController.navigate("plan") }, // Add padding around the text
            contentAlignment = Alignment.Center // Center the content horizontally and vertically
        ) {
            Text(
                text = "View Plan",
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                color = Color.Black
            )
        }
    }
}


