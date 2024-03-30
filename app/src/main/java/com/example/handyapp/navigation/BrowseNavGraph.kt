package com.example.handyapp.navigation

import MyTasksScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.handyapp.home.Settings.SettingsScreen
import com.example.handyapp.home.myRequests.MyRequestsScreen
import com.example.handyapp.home.myRequests.NotificationsScreen
import com.example.handyapp.home.presentation.WaitingScreen
import com.plcoding.m3_bottomnavigation.HomeScreen

@Composable
fun BrowseNavGraph(rootNavController: NavHostController , browseNavController: NavHostController){
    NavHost(navController = browseNavController, startDestination = Screen.Home.route){
        composable(
            route = Screen.Home.route
        ) {
            HomeScreen(browseNavController)
        }
        /*composable(
            route = Screen.Waiting.route
        ) {
            WaitingScreen(browseNavController)
        }*/
        composable(
            route = Screen.MyTasks.route
        ) {
            MyTasksScreen(browseNavController)
        }
        composable(
            route = Screen.MyRequests.route
        ) {
            MyRequestsScreen(browseNavController)
        }
        composable(
            route = Screen.Notifications.route
        ) {
            NotificationsScreen(browseNavController)
        }
        composable(
            route = Screen.Settings.route
        ) {
            SettingsScreen(navController = browseNavController)
        }
    }
}