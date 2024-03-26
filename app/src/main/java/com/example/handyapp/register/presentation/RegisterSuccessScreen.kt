package com.example.handyapp.register.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.example.handyapp.navigation.Screen

@Composable
fun RegisterSuccessScreen(navController: NavController){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        ) {
        Text(text = "Registration Succeeded !")
        Button(onClick = {
            navController.navigate(Screen.Login.route)
        }) {
            Text(text = "Go back to login")
        }
    }
}