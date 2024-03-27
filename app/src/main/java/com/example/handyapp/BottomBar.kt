package com.example.handyapp

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

/*@Composable
fun BottomBar(){

    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Songs", "Artists", "Playlists")

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Favorite, contentDescription = item) },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = { selectedItem = index }
            )
        }
    }
}*/
@Composable
fun BottomBar(navController: NavHostController) {
    val lists = listOf(
        BottomScreenNavigation.Home,
        BottomScreenNavigation.Search,
        BottomScreenNavigation.Booking,
        BottomScreenNavigation.Settings,
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDetination = lists.any { it.route == currentDestination?.route }

    if (bottomBarDetination) {
        NavigationBar(containerColor = MaterialTheme.colorScheme.background) {
            lists.forEach { screen ->
                val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                NavigationBarItem(
                    selected = selected,
                    onClick = { navController.navigate(screen.route){
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    } },
                    icon = { Icon(painter = painterResource(id =  if (selected) screen.selectedIcon else screen.unselectedIcon), contentDescription = screen.title) },
                    label = { Text(text = screen.title , maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 12.sp)},
                    colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.surface)
                )
            }
        }
    }
}
