//package com.example.handyapp.navigation
//
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Icon
//import androidx.compose.material3.NavigationBar
//import androidx.compose.material3.NavigationBarItem
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.navigation.NavDestination.Companion.hierarchy
//import androidx.navigation.NavGraph.Companion.findStartDestination
//import androidx.navigation.Navigation
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.currentBackStackEntryAsState
//import androidx.navigation.compose.rememberNavController
//import com.example.handyapp.home.myRequests.MyRequestsScreen
//import com.example.handyapp.home.myRequests.MyTasksScreen
//import com.example.handyapp.home.myRequests.NotificationsScreen
//
//@Composable
//fun AppNavigation() {
//    val navController = rememberNavController()
//
//    Scaffold(
//        bottomBar = {
//            NavigationBar {
//                val navBackStackEntry by navController.currentBackStackEntryAsState()
//                val currentDestination = navBackStackEntry?.destination>
//                listOfNavItem.forEach{ navItem ->
//                    NavigationBarItem(
//                        selected = currentDestination?.hierarchy?.any{it.route == navItem.route} == true,
//                        onClick = {
//                                  navController.navigate(navItem.route){
//                                      popUpTo(navController.graph.findStartDestination().id){
//                                          saveState = true
//                                      }
//                                      launchSingleTop = true
//                                      restoreState = true
//                                  }
//                                  },
//                        icon = {
//                               Icon(
//                                   imageVector = navItem.icon,
//                                   contentDescription = null
//                               )
//                               },
//                        label = {
//                                Text(text = navItem.label)
//                        })
//                }
//            }
//        }
//    ) { paddingValues ->
//        NavHost(
//            navController = navController,
//            startDestination = Screen.MyRequests.route,
//            modifier = Modifier.padding(paddingValues)
//        ) {
//            composable(route = Screen.MyRequests.route) {
//                MyRequestsScreen()
//            }
//            composable(route = Screen.MyTasks.route) {
//                MyTasksScreen()
//            }
//            composable(route = Screen.Notifications.route) {
//                NotificationsScreen()
//            }
//        }
//    }
//}