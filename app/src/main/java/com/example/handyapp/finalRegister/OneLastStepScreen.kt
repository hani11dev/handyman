package com.example.handyapp.finalRegister

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.handyapp.navigation.Screen


@Composable
fun OneLastStepScreen(navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Congratulations!, your request has been approved, you need to provide some more data to start using your account")
        Button(onClick = {
                navController.navigate(Screen.FinalRegistrationScreen.route)
        }) {
            Text(text = "Start Now")
        }
    }
}