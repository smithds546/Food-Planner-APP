package com.example.foodplanner.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.FileOutputStream


fun saveImageToStorage(context: Context, bitmap: Bitmap): Uri {
    val fileName = "meal_image_${System.currentTimeMillis()}.jpg"
    val outputDir = context.cacheDir
    val outputFile = File(outputDir, fileName)

    val outputStream = FileOutputStream(outputFile)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    outputStream.flush()
    outputStream.close()

    return Uri.fromFile(outputFile)
}

