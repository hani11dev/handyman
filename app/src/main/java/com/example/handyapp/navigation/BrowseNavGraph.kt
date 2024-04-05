package com.example.handyapp.navigation

import JobsScreen
import MyRequestsScreen
import MyTasksScreen
import RegisterInfoScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.handyapp.finalRegister.FinalRegisterScreen
import com.example.handyapp.finalRegister.FinishedSetupScreen
import com.example.handyapp.finalRegister.OneLastStepScreen
import com.example.handyapp.home.Settings.SettingsScreen
import com.example.handyapp.home.jobs.JobDetailsScreen
import com.example.handyapp.home.myRequests.NotificationsScreen
import com.example.handyapp.home.presentation.RefusedScreen
import com.example.handyapp.home.presentation.WaitingScreen

@Composable
fun BrowseNavGraph(rootNavController: NavHostController , browseNavController: NavHostController , startDestination : String , subStartDestination : String){
    NavHost(navController = browseNavController, startDestination = startDestination ){
        navigation(route = Graph.Home.route , startDestination = Screen.MyRequests.route){
            composable(
                route = Screen.Home.route
            ) {
                JobsScreen(browseNavController)
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
                SettingsScreen(rootNavController, browseNavController)
            }
            composable(
                route = Screen.JobsDetails.route + "/{jobID}"
            ) {
                JobDetailsScreen()
            }
        }
        navigation(route = Graph.State.route , startDestination = subStartDestination){
            composable(route = Screen.Waiting.route){
                WaitingScreen(navController = browseNavController , rootNavController = rootNavController)
            }
            composable(route = Screen.Refused.route){
                RefusedScreen(navController = browseNavController)
            }

            composable(route = Screen.OneLastStepScreen.route){
                OneLastStepScreen(navController = browseNavController)
            }
            composable(route = Screen.FinalRegistrationScreen.route){
                FinalRegisterScreen(navController = browseNavController)
            }
            composable(route = Screen.FinishedSetupScreen.route){
                FinishedSetupScreen(navController = rootNavController)
            }
            composable(
                route = Screen.RegisterInfo.route
            ) {
                RegisterInfoScreen(context = LocalContext.current, navController = browseNavController)
            }

        }

        /*navigation(startDestination = Screen.Waiting.route , route = Graph.Waiting.route){
            composable(route = Screen.Waiting.route){
                WaitingScreen(navController = browseNavController)
            }


        }
        navigation(startDestination = Screen.Refused.route , route = Graph.Refused.route){
            composable(route = Screen.Refused.route){
                RefusedScreen(navController = browseNavController)
            }
        }
        navigation(startDestination = Screen.OneLastStepScreen.route , route = Graph.Accepted.route){
            composable(route = Screen.OneLastStepScreen.route){
                OneLastStepScreen(navController = browseNavController)
            }
            composable(route = Screen.FinishedSetupScreen.route){
                FinishedSetupScreen(navController = browseNavController)
            }
            composable(route = Screen.FinalRegistrationScreen.route){
                FinalRegisterScreen(navController = browseNavController)
            }
        }*/
    }
}