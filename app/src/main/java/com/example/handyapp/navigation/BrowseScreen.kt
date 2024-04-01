package com.example.handyapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.handyapp.BottomBar

@Composable
fun BrowseScreen(rootNavController : NavHostController , browseNavController : NavHostController = rememberNavController() , viewModel: BrowseViewModel = hiltViewModel()){
    val startDestin = viewModel.startDestination
    val subStartDestin = viewModel.subStartDestination
    Scaffold(bottomBar ={
        BottomBar(navController = browseNavController)
    }) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)){
            BrowseNavGraph(rootNavController = rootNavController , browseNavController = browseNavController ,startDestin, subStartDestin)
        }
    }
}