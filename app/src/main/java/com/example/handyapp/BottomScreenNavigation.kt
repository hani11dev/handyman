package com.example.handyapp

import com.example.handyapp.navigation.Screen

sealed class BottomScreenNavigation(val title : String , val selectedIcon : Int,val unselectedIcon : Int ,val route : String) {
    object Jobs : BottomScreenNavigation(title = "Jobs" , selectedIcon = R.drawable.filled_home, unselectedIcon = R.drawable.outlined_home , route = Screen.Jobs.route)
    object Requests : BottomScreenNavigation(title = "Requests" , selectedIcon = R.drawable.outlined_request_icon,unselectedIcon = R.drawable.outlined_request_icon , route = Screen.MyRequests.route)
    object Settings : BottomScreenNavigation(title = "Settings" , selectedIcon = R.drawable.filled_settings, unselectedIcon = R.drawable.outlined_settings , route = Screen.Settings.route)
    object MyTasks : BottomScreenNavigation(title = "My Tasks" , selectedIcon = R.drawable.filled_task_icon ,unselectedIcon = R.drawable.outlined_task_icon , route = Screen.MyTasks.route)
}