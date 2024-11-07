package com.example.foodplanner

import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat


@Composable
fun CameraScreen() {
    CameraIntent()
}

@Composable
fun CameraIntent() {
    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { newImage ->
            bitmap = newImage
        }
    )
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch()
        }
    }
    Column {
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    //.size(36.dp)
            )
        }

        val context = LocalContext.current

        TextButton(
            onClick = {
                // Checks if the permission is granted
                val permissionCheckResult =
                    ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)

                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    // The permission is already granted
                    cameraLauncher.launch()
                } else {
                    // Launches the permission request
                    permissionLauncher.launch(android.Manifest.permission.CAMERA)
                }
            }
        ) {
            Text(
                text = "Use camera"
            )
        }
    }
}