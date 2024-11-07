package com.example.foodplanner.meal

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.foodplanner.data.Meal
import com.example.foodplanner.data.MealViewModel
import com.example.foodplanner.utils.saveImageToStorage
import java.io.File


@Composable
fun MealApp(navController: NavController) {
    val mealViewModel: MealViewModel = viewModel()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MealList(
            navController = navController,
            mealViewModel = mealViewModel,
            onMealClick = { meal ->
                navController.navigate("MealDetailCard/${meal.id}")
            }
        )
    }
}



@Composable
fun MealList(mealViewModel: MealViewModel, onMealClick: (Meal) -> Unit, navController: NavController) {
    val meals by mealViewModel.allMeals.observeAsState(initial = emptyList())

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(8.dp), // Move the modifier inside LazyVerticalGrid
            content = {
                items(meals.size) { index ->
                    MealItem(
                        meal = meals[index],
                        onDeleteClick = { mealViewModel.delete(it) },
                        onMealClick = onMealClick,
                        onPhotoSelected = { _, _ -> }, // Placeholder for onPhotoSelected
                        navController = navController
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun MealItem(
    meal: Meal,
    onDeleteClick: (Meal) -> Unit,
    onMealClick: (Meal) -> Unit,
    onPhotoSelected: (Meal, Uri) -> Unit, // Callback for handling photo selected event
    navController: NavController
) {
    // State to track whether the long-press gesture is detected
    var isLongPressed by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(8.dp)
            .size(150.dp) // Adjust the size of the box as needed
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        isLongPressed = true
                    }, // Set isLongPressed to true on long press
                    onTap = { isLongPressed = false } // Reset isLongPressed on tap
                )
            }
            .clickable { onMealClick(meal) }
    ) {
        // Show dialog box when long-press is detected
        if (isLongPressed) {
            //ShowAddPhotoDialog(navController)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Display meal image if available
            if (meal.imageUri != null) {
                val painter = rememberImagePainter(data = meal.imageUri)
                Image(
                    painter = painter,
                    contentDescription = "Meal Image",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Default Meal Icon",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                )
            }

            // Display meal name
            Text(
                text = meal.food,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Delete button
            IconButton(
                onClick = { onDeleteClick(meal) },
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}






@OptIn(ExperimentalCoilApi::class, ExperimentalFoundationApi::class)
@Composable
fun MealDetailCard(
    meal: Meal,
    onCloseClicked: () -> Unit, // Callback for handling close button click
    viewModel: MealViewModel // Inject the MealViewModel instance

) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val showDialog = remember { mutableStateOf(false) }


    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { newImage ->
            bitmap = newImage
            if (newImage != null) {
                val imageUri = saveImageToStorage(context, newImage)
                viewModel.updateMealWithImageUri(meal, imageUri.toString())
                onCloseClicked() // Close the card after taking a photo

            }
        }
    )
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch()
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier,
                    onClick = {
                        // Initiate sharing action
                        val uri = meal.imageUri?.let { Uri.parse(it) }
                        val contentUri = uri?.let { FileProvider.getUriForFile(context, "${context.packageName}.provider", File(it.path)) }
                        val mealDetails = "${meal.food} - Time to cook: ${meal.time} - Nutritional Value: ${meal.nutrition}"
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_SUBJECT, "Check out my meal details!")
                            putExtra(Intent.EXTRA_TEXT, mealDetails)
                            type = "text/plain"
                            contentUri?.let {
                                putExtra(Intent.EXTRA_STREAM, it)
                                type = "image/*"
                            }
                        }

                        val shareIntent = Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)
                        onCloseClicked()
                    }
                ) {
                    Icon(imageVector = Icons.Filled.Share, contentDescription = "Share")
                }

                // Close button at the top right
                IconButton(
                    onClick = onCloseClicked,
                    modifier = Modifier
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }


            Text(
                text = meal.food,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = meal.type,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Nutritional Value: ${meal.nutrition}",
            )
            Text(
                text = "Time to Cook: ${meal.time} minutes",
            )

            if (meal.imageUri != null) {
                val painter = rememberImagePainter(
                    data = meal.imageUri,
                    builder = {
                        crossfade(true)
                    }
                )

                Image(
                    painter = painter,
                    contentDescription = "Meal Image",
                    modifier = Modifier
                        .clip(RectangleShape)
                        .size(200.dp)
                        .combinedClickable(
                            onClick = {},
                            onLongClick = { showDialog.value = true }
                        )
                )
            } else {
                Text(text = "")

                val context = LocalContext.current

                Button(
                    onClick = {
                        // Checks if the permission is granted
                        val permissionCheckResult =
                            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

                        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                            // The permission is already granted
                            cameraLauncher.launch()
                        } else {
                            // Launches the permission request
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                ) {
                    Text(
                        text = "Take Photo"
                    )
                }
            }
        }
    }
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Photo Options") },
            text = { Text("What would you like to do with the photo?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Checks if the permission is granted
                        val permissionCheckResult =
                            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

                        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                            // The permission is already granted
                            cameraLauncher.launch()
                        } else {
                            // Launches the permission request
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                        showDialog.value = false
                    }
                ) {
                    Text("Retake Photo")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        // Handle delete photo functionality
                        viewModel.updateMealWithImageUri(meal, null)
                        showDialog.value = false
                        onCloseClicked() // Close the card after taking a photo
                    }
                ) {
                    Text("Delete Photo")
                }
            }
        )
    }
}


@Composable
fun MealDetailScreen(
    meal: Meal?,
    onCloseClicked: () -> Unit, // Callback for handling close button click
    viewModel: MealViewModel // Add the ViewModel parameter

) {
    if (meal != null) {
        MealDetailCard(meal, onCloseClicked = onCloseClicked, viewModel = viewModel)
    }
}
