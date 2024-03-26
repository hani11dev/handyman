package com.example.handyapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

val listOfNavItem = listOf(
    NavItem(
        label = "MyRequests",
        icon = Icons.Default.Email,
        route = "myRequests"
    ),
    NavItem(
        label = "MyTasks",
        icon = Icons.Default.DateRange,
        route = "myTasks"
    ),
    NavItem(
        label = "Notifications",
        icon = Icons.Default.Notifications,
        route = "notifications"
    )
    )
