package com.example.handyapp.home.myRequests

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MyRequestsScreen(navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        TopBar(
            title = "Requests",
            navigationIcon = Icons.Default.ArrowBack,
            onNavigationIconClick = {
                // Handle navigation icon click
            },
            navigationIconSize = 43.dp,
            titleTextStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold) // Set the title text style to bold
        )

        // Use LazyColumn instead of Column for better performance
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            /*items(requests) { request ->
                RequestItem(request = request)
            }*/
            items(requests.size) {
                    request->
                RequestItem(requests[request])}
        }


    }
}