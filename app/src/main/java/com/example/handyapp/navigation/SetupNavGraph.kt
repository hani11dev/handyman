package com.example.handyapp.navigation

import RegisterInfoScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.handyapp.login.presentation.LoginScreen
import com.example.handyapp.register.presentation.RegisterScreen1

import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.handyapp.home.presentation.WaitingScreen
import com.example.handyapp.register.presentation.RegisterSuccessScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Graph.Auth.route
    ) {
        navigation(route = Graph.Auth.route, startDestination = Screen.Login.route) {
            composable(
                route = Screen.Login.route
            ) {
                LoginScreen(navController)
            }
            composable(
                route = Screen.Register.route
            ) {
                RegisterScreen1(navController)
            }
            composable(
                route = Screen.RegisterInfo.route
            ) {
                RegisterInfoScreen(context = LocalContext.current, navController = navController)
            }
            composable(
                route = Screen.RegistrationSucceeded.route
            ) {
                RegisterSuccessScreen(navController = navController)
            }
        }
        composable(route = Graph.Browse.route){
            BrowseScreen(rootNavController = navController)
        }
        navigation(startDestination = Screen.Waiting.route , route = Graph.Waiting.route){
            composable(route = Screen.Waiting.route){
                WaitingScreen(navController = navController)
            }
        }

    }
}