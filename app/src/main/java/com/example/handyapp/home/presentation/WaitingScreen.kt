package com.example.handyapp.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.handyapp.navigation.Graph
import com.example.handyapp.navigation.Screen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun WaitingScreen(navController: NavHostController , rootNavController: NavController){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Text(text = "Your request has been submitted successfully")
        Text(text = "wait until admins review your request")
        Button(onClick = {
            FirebaseAuth.getInstance().signOut()
            rootNavController.navigate(Graph.Auth.route)
        }) {
            Text(text = "Logout")
        }
    }
}