package com.example.foodplanner.appBars

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BottomAppBar(navController: NavController) {
    androidx.compose.material3.BottomAppBar(
        actions = {
            IconButton(
                onClick = { navController.navigate("main") },
                modifier = Modifier.padding(8.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Filled.Home, contentDescription = "Home")

                }
            }

            IconButton(
                onClick = { navController.navigate("mealSplash") },
                modifier = Modifier.padding(8.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Filled.Create, contentDescription = "Meals")

                }
            }

            IconButton(
                onClick = { navController.navigate("planner") },
                modifier = Modifier.padding(8.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Filled.DateRange, contentDescription = "Planner")

                }
            }

        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("NewMeal") },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Filled.Add, "Localized description")
            }
        }
    )
}

