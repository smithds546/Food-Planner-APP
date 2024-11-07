package com.example.foodplanner
//no longer use, began using firebase to save all meals to.

// Import the necessary Firebase libraries
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// Define the database URL
private const val DATABASE_URL = "https://food-planner-f0545-default-rtdb.europe-west1.firebasedatabase.app/"

// Define a function to write data to the database
fun writeToDTB(textField1Value: String, textField2Value: String, textField3Value: String) {
    // Get a reference to the Firebase Realtime Database with the specified URL
    val database = Firebase.database(DATABASE_URL)

    // Get a reference to the location in the database where you want to write the data
    val myRef = database.getReference("meal")

    // Create a map to hold the data to be written to the database
    val dataMap = mapOf(
        "firstField" to textField1Value,
        "nutrition" to textField2Value,
        "time" to textField3Value
    )

    // Write the data to the database
    myRef.push().setValue(dataMap)
        .addOnSuccessListener {
            // Data successfully written to the database
            println("Data successfully written to the database.")
        }
        .addOnFailureListener { error ->
            // Failed to write data to the database
            println("Error writing data to the database: $error")
        }
}


// Define the function to read data from the database
fun readDTB() {
    // Get a reference to the Firebase Realtime Database with the specified URL
    val database = Firebase.database(DATABASE_URL)

    // Get a reference to the location in the database from which you want to read data
    val myRef = database.getReference("message")

    // Add a ValueEventListener to listen for changes in the data at this location
    myRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            val value = dataSnapshot.getValue<String>()
            Log.d(TAG, "Value is: $value")
        }

        override fun onCancelled(error: DatabaseError) {
            // Failed to read value
            Log.w(TAG, "Failed to read value.", error.toException())
        }
    })
}
