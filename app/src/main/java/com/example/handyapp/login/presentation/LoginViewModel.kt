package com.example.handyapp.login.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.handyapp.R
import com.example.handyapp.navigation.Graph
import com.example.handyapp.navigation.Screen
import com.example.handyapp.register.domain.components.LoginEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class LoginViewModel(
    val navController: NavController,

): ViewModel(){


    var state by mutableStateOf(LoginState())
    fun Login() {
        Firebase.auth.signInWithEmailAndPassword(state.email, state.password)
            .addOnCompleteListener { task ->
                navController.navigate(Graph.Browse.route)
                /*val db = FirebaseFirestore.getInstance()
// Get the current user ID (assuming the user is authenticated)
                val currentUser = FirebaseAuth.getInstance().currentUser
                val userId = currentUser?.uid
                if (userId != null) {
                    // Specify the document reference using the user ID
                    val docRef = db.collection("handymen").document(userId) // Use user ID as document ID
                    // Get the document
                    docRef.get()
                        .addOnSuccessListener { documentSnapshot ->
                            if (documentSnapshot.exists()) {
                                // Document exists, retrieve data
                                val data = documentSnapshot.getString("status") // Map<String, Any> containing document data
                                if(data != null){
                                    if(data == "NEW"){
                                        navController.navigate(Screen.RegisterInfo.route)
                                    }else if(data == "WAITING"){
                                        navController.navigate(Graph.Waiting.route)
                                    }else if(data == "ACCEPTED"){
                                        navController.navigate(Graph.Accepted.route)
                                    }else if(data == "REFUSED"){
                                        navController.navigate(Graph.Refused.route)
                                    }else if(data == "ACTIVE"){
                                        navController.navigate(Graph.Browse.route)
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
                } else {
                    Log.d("Login", "login failed")
                }*/
            }
    }
    fun onEvent(event: LoginEvent){
        when(event){
            is LoginEvent.EmailChanged->{
                state = state.copy(email = event.email)
            }
            is LoginEvent.PasswordChanged->{

                state = state.copy(password = event.password)
            }

            is LoginEvent.Submit->{
              Login()
            }

            else -> {}
        }
    }
    sealed class ValidationEvent{
        object Success: ValidationEvent()
    }
    }
