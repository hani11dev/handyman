package com.example.handyapp.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun WaitingScreen(){
    Column {
        Text(text = "Your request has been submitted successfully")
        Text(text = "wait until admins review your request")
    }
}