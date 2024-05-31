package com.example.handyapp.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.handyapp.BottomBar
import com.example.handyapp.BottomScreenNavigation
import com.example.handyapp.R
import com.example.handyapp.Response
import com.example.handyapp.home.Settings.SettingsViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseScreen(
    rootNavController: NavHostController,
    browseNavController: NavHostController = rememberNavController(),
    viewModel: BrowseViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    context: Context = LocalContext.current
    //startDestination : String
) {

    val startDestin = viewModel.startDestination
    val subStartDestin = viewModel.subStartDestination

    val navBackStackEntry by browseNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val enabledGestureScreen = listOf(
        BottomScreenNavigation.Jobs,
        BottomScreenNavigation.Settings,
        BottomScreenNavigation.MyTasks,
        BottomScreenNavigation.Requests,
        //Screen.Jobs
    )

    when (subStartDestin) {
        "" -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        else -> {
            val items = listOf(
                NavigationItem(
                    title = "Profile",
                    selectedIcon = R.drawable.user_square,
                    route = Screen.ProfileSettings.route
                ),
                NavigationItem(
                    title = "Saved Jobs",
                    selectedIcon = R.drawable.outlined_save,
                    route = Screen.SavedJobs.route
                ),
            )
            var selectedItemIndex by rememberSaveable {
                mutableStateOf(0)
            }
            ModalNavigationDrawer(
                gesturesEnabled =/* if (enabledGestureScreen.all { it.route == currentDestination?.route }) true else*/ false,
                drawerContent = {
                    ModalDrawerSheet(drawerContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest) {
                        IconButton(onClick = { scope.launch { drawerState.close() } }) {
                            Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
                        }
                        when (val resp = settingsViewModel.handyManSettingsInfo.value) {
                            is Response.onLoading -> {}
                            is Response.onFaillure -> {}
                            is Response.onSuccess -> {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(128.dp)
                                        .padding(horizontal = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = resp.data.get("ProfileImage"),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(96.dp)
                                            .clip(CircleShape)
                                            .border(2.dp, Color.Black, CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text(
                                            text = resp.data["FirstName"] + " " + resp.data["LastName"],
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 20.sp
                                        )
                                        Text(text = resp.data["Email"] ?: "")
                                    }
                                }
                            }
                        }


                        Spacer(modifier = Modifier.height(16.dp))
                        items.forEachIndexed { index, item ->
                            NavigationDrawerItem(
                                label = {
                                    Text(text = item.title)
                                },
                                selected = false,//index == selectedItemIndex,
                                onClick = {
                                    browseNavController.navigate(item.route) {
                                        launchSingleTop = true
                                        popUpTo(item.route) {
                                            inclusive = true
                                        }

                                    }
//                                            navController.navigate(item.route)
                                    //selectedItemIndex = index
                                    scope.launch {
                                        drawerState.close()
                                    }
                                },
                                icon = {
                                    Icon(
                                        painter = painterResource(id = item.selectedIcon),
                                        contentDescription = item.title
                                    )
                                },
                                badge = {
                                    item.badgeCount?.let {
                                        Text(text = item.badgeCount.toString())
                                    }
                                },
                                modifier = Modifier
                                    .padding(NavigationDrawerItemDefaults.ItemPadding)

                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        var progressState = rememberSaveable {
                            mutableStateOf(false)
                        }
                        Button(
                            onClick = {
                                settingsViewModel.signOut()
                                when (val resp = settingsViewModel.signOutState.value) {
                                    is Response.onLoading -> {
                                        progressState.value = true
                                    }

                                    is Response.onFaillure -> {
                                        Toast.makeText(context, resp.message, Toast.LENGTH_SHORT)
                                            .show()
                                    }

                                    is Response.onSuccess -> {
                                        rootNavController.navigate(Graph.Auth.route) {
                                            popUpTo(Graph.Browse.route) {
                                                inclusive = true
                                            }
                                        }

                                    }
                                }


                            }, modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 8.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row {
                                Text(text = "Log Out")
                                if (progressState.value)
                                    CircularProgressIndicator()
                            }
                        }
                    }
                },
                drawerState = drawerState
            ) {
                Scaffold(bottomBar = {
                    BottomBar(navController = browseNavController)
                },
                    topBar = {
                        //if (currentDestination?.route == BottomScreenNavigation.Jobs.route) {
                        val currentDestinationRoute = currentDestination?.route ?: ""
                        TopAppBar(
                            title = {
                                Text(
                                    if (currentDestinationRoute == Screen.JobsDetails.route + "/{jobID}") "Job Details"
                                    else if (currentDestinationRoute == Screen.ReportScreen.route + "/{taskID}/{clientID}") "Report"
                                    else if (currentDestinationRoute == Screen.DetailRequest.route + "/{requestID}") "Request Details"
                                    else if (currentDestinationRoute == Screen.BidScreen.route + "/{jobID}") "Bid"
                                    else if (currentDestinationRoute == Screen.FinalRegistrationScreen.route) "Final Registration"
                                    else if (currentDestinationRoute == Screen.RegisterInfo.route) "Register Info"
                                    else if (currentDestinationRoute == Screen.ChatScreen.route + "/{ClientID}") "Chat"
                                    else currentDestination?.route ?: ""
                                )
                            },

                            /*actions = {
                                *//*IconButton(onClick = { browseNavController.navigate(Screen.NotificationScreen.route) }) {
                                        Icon(imageVector = Icons.Outlined.Notifications, contentDescription = null)
                                    }*//*
                                    IconButton(onClick = {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    }) {
                                        Icon(imageVector = Icons.Outlined.Notifications, contentDescription = null)
                                    }
                                },*/
                            navigationIcon = {
                                when(currentDestination?.route){
                                    BottomScreenNavigation.Jobs.route ,
                                    BottomScreenNavigation.Requests.route ,
                                    BottomScreenNavigation.MyTasks.route ,
                                    BottomScreenNavigation.Settings.route -> {
                                        IconButton(onClick = {
                                            scope.launch {
                                                drawerState.open()
                                            }
                                        }) {
                                            Icon(
                                                imageVector = Icons.Outlined.Menu,
                                                contentDescription = null
                                            )
                                        }

                                    }
                                    else ->{
                                        IconButton(onClick = {
                                            browseNavController.navigateUp()
                                        }) {
                                            Icon(
                                                imageVector = Icons.Filled.ArrowBack,
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }
                            },
                            actions = {
                                when(currentDestination?.route){
                                    BottomScreenNavigation.Jobs.route ,
                                    BottomScreenNavigation.Requests.route ,
                                    BottomScreenNavigation.MyTasks.route ,
                                    BottomScreenNavigation.Settings.route ->{
                                        IconButton(onClick = { browseNavController.navigate(Screen.NotificationScreen.route) }) {
                                            Icon(imageVector = Icons.Outlined.Notifications, contentDescription = null)
                                        }
                                    }
                                    else -> {}
                                }
                                /*if (currentDestination?.route.equals(BottomScreenNavigation.Jobs.route) || currentDestination?.route.equals(BottomScreenNavigation.MyTasks.route)){
                                    IconButton(onClick = { browseNavController.navigate(Screen.NotificationScreen.route) }) {
                                        Icon(imageVector = Icons.Outlined.Notifications, contentDescription = null)
                                    }

                                }*/

                            }
                        )

                        //}
                    }) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
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


}

data class NavigationItem(
    val title: String,
    val selectedIcon: Int,
    val badgeCount: Int? = null,
    val route: String
)

