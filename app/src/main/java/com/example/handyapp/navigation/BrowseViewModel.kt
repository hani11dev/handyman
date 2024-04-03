package com.example.handyapp.navigation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BrowseViewModel @Inject constructor() : ViewModel(){
    var startDestination by mutableStateOf(Graph.State.route)
        private set

    var subStartDestination by mutableStateOf("")
        private set

    init {
        val db = FirebaseFirestore.getInstance()
// Get the current user ID (assuming the user is authenticated)
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid
        val docRef = db.collection("HandyMan").document(userId!!) // Use user ID as document ID
        // Get the document
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Document exists, retrieve data
                    val data = documentSnapshot.getString("Status") // Map<String, Any> containing document data
                    if(data != null){
                        if(data == "NEW"){
                            //navController.navigate(Screen.RegisterInfo.route)
                            startDestination = Graph.State.route
                            subStartDestination = Screen.RegisterInfo.route
                        }else if(data == "WAITING"){
                            //navController.navigate(Graph.Waiting.route)
                            startDestination = Graph.State.route
                            subStartDestination = Screen.Waiting.route
                        }else if(data == "ACCEPTED"){
                            //navController.navigate(Graph.Accepted.route)
                            startDestination = Graph.State.route
                            subStartDestination = Screen.OneLastStepScreen.route
                        }else if(data == "REFUSED"){
                            //navController.navigate(Graph.Refused.route)
                            startDestination = Graph.State.route
                            subStartDestination = Screen.Refused.route
                        }else if(data == "ACTIVE"){
                            //navController.navigate(Graph.Browse.route)
                            startDestination = Graph.Home.route
                            subStartDestination = Screen.Waiting.route
                        }
                    }
                    // You can further process the retrieved document data here
                } else {
                    println("Document does not exist")
                }
            }
            .addOnFailureListener { e ->
                Log.d("Login", "login failed")
            }
    }
}