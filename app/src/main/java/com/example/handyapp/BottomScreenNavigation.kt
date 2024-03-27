package com.example.handyapp

import com.example.handyapp.navigation.Screen

sealed class BottomScreenNavigation(val title : String , val selectedIcon : Int,val unselectedIcon : Int ,val route : String) {
    object Home : BottomScreenNavigation(title = "Home" , selectedIcon = R.drawable.filled_home, unselectedIcon = R.drawable.outlined_home , route = Screen.Home.route)
    object Booking : BottomScreenNavigation(title = "Booking" , selectedIcon = R.drawable.filled_booking,unselectedIcon = R.drawable.outlined_booking , route = Screen.MyRequests.route)
    object Settings : BottomScreenNavigation(title = "Settings" , selectedIcon = R.drawable.filled_settings, unselectedIcon = R.drawable.outlined_settings , route = Screen.Settings.route)
    object Search : BottomScreenNavigation(title = "Search" , selectedIcon = R.drawable.filled_search ,unselectedIcon = R.drawable.outlined_search , route = Screen.MyTasks.route)
}