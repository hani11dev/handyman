package com.example.handyapp.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.handyapp.navigation.Graph
import com.example.handyapp.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Composable
fun RefusedScreen(navController: NavController){
    val refuseReasonState = remember { mutableStateOf("") }

    // Use LaunchedEffect to launch a coroutine when this composable is first composed
    LaunchedEffect(true) {
        // Fetch data from Firestore within the coroutine launched by LaunchedEffect
        val reason = fetchDataFromFirestore()
        // Update the mutable state with the fetched data
        refuseReasonState.value = reason
    }

    // Display the fetched data
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Refuse Reason: ${refuseReasonState.value}")
        Button(onClick = {
            runBlocking {
                FirebaseAuth.getInstance().signOut()
            }
            navController.navigate(Graph.Auth.route){
                popUpTo(Graph.Refused.route) {
                    inclusive = true
                    saveState = true
                }
            }
        }) {
            Text(text = "Sign out")
        }
        Button(onClick = {
            navController.navigate(Screen.RegisterInfo.route){
                popUpTo(Graph.Refused.route) {
                    inclusive = true
                    saveState = true
                }
            }
        }) {
            Text(text = "make a new request")
        }
    }
}

suspend fun fetchDataFromFirestore(): String {
    return withContext(Dispatchers.IO) {
        suspendCancellableCoroutine<String> { continuation ->
            val db = FirebaseFirestore.getInstance()
            val currentUser = FirebaseAuth.getInstance().currentUser
            val userId = currentUser?.uid
            if (userId != null) {
                val docRef = db.collection("handymen").document(userId)
                docRef.get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val reason = documentSnapshot.getString("refuse_reason") ?: ""
                            continuation.resume(reason)
                        } else {
                            continuation.resume("")
                        }
                    }
                    .addOnFailureListener { e ->
                        continuation.resumeWithException(e)
                    }
            } else {
                continuation.resume("")
            }

            // Handle cancellation of coroutine
            continuation.invokeOnCancellation {
                // Cancel Firestore listener if needed
            }
        }
    }
}