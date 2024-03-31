package com.example.handyapp.home.myRequests


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun MyApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "MyRequestScreen") {
        composable("MyRequestScreen") {

        }
        composable("Detail") {

        }
    }

}