package com.example.foodplanner.meal

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.foodplanner.appBars.BottomAppBar
import com.example.foodplanner.appBars.TopAppBar
import com.example.foodplanner.data.Meal
import com.example.foodplanner.data.MealViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MealsPage(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(navController, title = "Meals")
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
                    Spacer(modifier = Modifier.height(6.dp))
                    Splash(navController)
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    )
}


@Composable
fun Splash(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        //color = Color(0xFF81D4FA), // Light blue,
        shape = RoundedCornerShape(25.dp)
    ) {
        MealApp(navController = navController)
    }
}



@Composable
fun AddMealDialog(
    navController: NavController,
    mealViewModel: MealViewModel,
    onAddMeal: (Meal) -> Unit
) {
    var mealName by rememberSaveable { mutableStateOf("") }
    var mealTime by rememberSaveable { mutableStateOf("") }
    var mealNutrition by rememberSaveable { mutableStateOf("") }
    var mealType by rememberSaveable { mutableStateOf(MealType.BREAKFAST) }

    Dialog(
        onDismissRequest = { navController.popBackStack() }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Meal Type",
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        MealTypeButton(
                            text = "Breakfast",
                            isSelected = mealType == MealType.BREAKFAST,
                            onClick = { mealType = MealType.BREAKFAST }
                        )
                        MealTypeButton(
                            text = "Lunch",
                            isSelected = mealType == MealType.LUNCH,
                            onClick = { mealType = MealType.LUNCH }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        MealTypeButton(
                            text = "Dinner",
                            isSelected = mealType == MealType.DINNER,
                            onClick = { mealType = MealType.DINNER }
                        )
                        MealTypeButton(
                            text = "Snack",
                            isSelected = mealType == MealType.SNACK,
                            onClick = { mealType = MealType.SNACK }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = mealName,
                    onValueChange = { mealName = it },
                    label = { Text("Meal Name") },
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                TextField(
                    value = mealTime,
                    onValueChange = { mealTime = it },
                    label = { Text("Time to cook") },
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                TextField(
                    value = mealNutrition,
                    onValueChange = { mealNutrition = it },
                    label = { Text("Nutritional Value") },
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            val mealTypeString = when (mealType) {
                                MealType.BREAKFAST -> "Breakfast"
                                MealType.LUNCH -> "Lunch"
                                MealType.DINNER -> "Dinner"
                                MealType.SNACK -> "Snack"
                            }
                            onAddMeal(Meal(type = mealTypeString, food = mealName, time = mealTime.toFloatOrNull() ?: 0f, nutrition = mealNutrition))
                            mealName = ""
                            mealTime = ""
                            mealNutrition = ""
                        }
                    ) {
                        Text("Add Meal")
                    }
                }
            }
        }
    }
}



@Composable
fun MealTypeButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text)
    }
}


enum class MealType {
    BREAKFAST, LUNCH, DINNER, SNACK
}

