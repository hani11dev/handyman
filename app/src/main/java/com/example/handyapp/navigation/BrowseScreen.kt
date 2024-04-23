package com.example.handyapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.handyapp.BottomBar
import com.example.handyapp.BottomScreenNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseScreen(
    rootNavController: NavHostController,
    browseNavController: NavHostController = rememberNavController(),
    viewModel: BrowseViewModel = hiltViewModel()
) {

    val startDestin = viewModel.startDestination
    val subStartDestin = viewModel.subStartDestination

    val navBackStackEntry by browseNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    when (subStartDestin) {
        "" -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        else -> {
            Scaffold(bottomBar = {
                BottomBar(navController = browseNavController)
            },
                topBar = {
                    if (currentDestination?.route == BottomScreenNavigation.Home.route) {
                        TopAppBar(
                            title = {
                                Text("Jobs")
                            },

                            actions = {
                                IconButton(onClick = { browseNavController.navigate(Screen.NotificationScreen.route) }) {
                                    Icon(imageVector = Icons.Outlined.Notifications, contentDescription = null)
                                }
                            }
                        )

                    }
                }) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    BrowseNavGraph(
                        rootNavController = rootNavController,
                        browseNavController = browseNavController,
                        startDestin,
                        subStartDestin
                    )
                }
            }
        }
    }


}

