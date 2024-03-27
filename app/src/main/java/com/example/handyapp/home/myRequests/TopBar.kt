package com.example.handyapp.home.myRequests

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material3.LocalTextStyle
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight


@Composable
fun TopBar(
    title: String,
    navigationIcon: ImageVector?,
    onNavigationIconClick: () -> Unit,
    navigationIconSize: Dp,
    titleTextStyle: TextStyle // Add a parameter for title text style
) {
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        navigationIcon?.let {
            Icon(
                imageVector = it,
                contentDescription = "Back",
                modifier = Modifier
                    .size(navigationIconSize)
                    .padding(end = 16.dp)
                    .clickable(onClick = onNavigationIconClick)
            )
        }
        Text(
            text = title,
            style = titleTextStyle // Apply the provided title text style
        )
    }
}







@Preview(showBackground = true)
@Composable
fun TopNavBar() {
    TopBar(
        title = "Requests",
        navigationIcon = Icons.Default.ArrowBack,
        onNavigationIconClick = {
            // Handle navigation icon click
        },
        navigationIconSize = 43.dp,
        titleTextStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold) // Set the title text style to bold
    )

}