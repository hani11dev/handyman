package com.example.handyapp.finalRegister

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.handyapp.R
import com.example.handyapp.navigation.Screen


@Composable
fun OneLastStepScreen(navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painter = painterResource(R.drawable.approved_illus), contentDescription = null  , contentScale = ContentScale.Crop,
            modifier = Modifier.size(256.dp))
        Text(text = "Congratulations!, your request has been approved, you need to provide some more data to start using your account")
        Button(onClick = {
                navController.navigate(Screen.FinalRegistrationScreen.route)
        }) {
            Text(text = "Start Now")
        }
    }
}