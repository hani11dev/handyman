package com.example.handyapp.home.myRequests


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController


@Composable
fun NotificationsScreen(navController: NavController){
    Column (
        Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally
    ){


        Text("My Requsts Screen")

    }
}