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
import com.example.handyapp.navigation.Graph
import com.example.handyapp.navigation.Screen

@Composable
fun FinishedSetupScreen(navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "You finished setting up your account !")
        Button(onClick = {
            navController.navigate(Graph.Browse.route){
                popUpTo(Graph.State.route){
                    inclusive = true
                }
            }
        }) {
            Text(text = "Go to home page")
        }
    }
}
