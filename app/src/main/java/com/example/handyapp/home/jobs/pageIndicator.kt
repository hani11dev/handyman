package com.example.handyapp.home.jobs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PageIndicator(pagesSize: Int,
    modifier: Modifier = Modifier,
    selectedPage: Int,
    selectedColor : Color = MaterialTheme.colorScheme.primary,
    unSelectedColor : Color = Color.LightGray
) {
    Row( modifier = modifier,horizontalArrangement = Arrangement.SpaceBetween) {
        repeat(pagesSize) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(if (it == selectedPage) selectedColor else unSelectedColor)
            )

        }
    }
}