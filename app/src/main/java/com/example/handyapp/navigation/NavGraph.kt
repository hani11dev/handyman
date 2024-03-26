package com.example.handyapp.navigation

import RegisterInfoScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.handyapp.MainActivity
import com.example.handyapp.login.presentation.LoginScreen
import com.example.handyapp.register.presentation.RegisterScreen1

import android.content.Context
import com.example.handyapp.home.myRequests.MyRequestsScreen
import com.example.handyapp.home.myRequests.MyTasksScreen
import com.example.handyapp.home.myRequests.NotificationsScreen
import com.example.handyapp.home.presentation.WaitingScreen
import com.example.handyapp.register.presentation.RegisterSuccessScreen
import com.plcoding.m3_bottomnavigation.HomeScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    context: Context
){
    NavHost(
        navController = navController,
        startDestination = Screen.Register.route
    ){
        composable(
            route = Screen.Login.route
        ){
            LoginScreen(navController)
        }
        composable(
            route = Screen.Register.route
        ){
            RegisterScreen1(navController)
        }
        composable(
            route = Screen.RegisterInfo.route
        ){
            RegisterInfoScreen(context =context, navController = navController)
        }
        composable(
            route = Screen.RegistrationSucceeded.route
        ){
            RegisterSuccessScreen(navController = navController)
        }
        composable(
            route = Screen.Home.route
        ){
            HomeScreen(navController)
        }
        composable(
            route = Screen.Waiting.route
        ){
            WaitingScreen()
        }
        composable(
            route = Screen.MyTasks.route
        ){
            MyTasksScreen(navController)
        }
        composable(
            route = Screen.MyRequests.route
        ){
            MyRequestsScreen(navController)
        }
        composable(
            route = Screen.Notifications.route
        ){
            NotificationsScreen(navController)
        }
    }
}