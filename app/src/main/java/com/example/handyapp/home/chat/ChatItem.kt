package com.example.handyapp.home.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatItem(message: Message, userID: String) {
    var showTimestamp by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = if (message.sender == userID) Alignment.End else Alignment.Start
    ) {
        if (message.type == "text") {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .clickable {
                        showTimestamp = !showTimestamp
                    }
                    .background(
                        if (message.sender == userID) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 8.dp,
                            bottomStart = if (message.sender == userID) 8.dp else 0.dp,
                            bottomEnd = if (message.sender == userID) 0.dp else 8.dp
                        )
                    )
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                AnimatedVisibility(visible = showTimestamp) {
                    androidx.compose.material3.Text(text = timestampToDate(message.timestamp))
                }
                androidx.compose.material3.Text(
                    text = message.text,
                    Modifier.padding(vertical = 4.dp, horizontal = 4.dp),
                    fontSize = 16.sp,
                    color = if (message.sender == userID) MaterialTheme.colorScheme.onBackground else Color.White
                )

            }

        } else {
            /*LazyHorizontalGrid(rows = GridCells.Adaptive(128.dp)) {
                items(message.images) {
                    AsyncImage(
                        model = it,
                        contentDescription = "image",
                        modifier = Modifier
                            .size(128.dp)
                            .heightIn(max = 1024.dp)
                    )
                }
            }*/
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .background(
                        if (message.sender == userID) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 8.dp,
                            bottomStart = if (message.sender == userID) 8.dp else 8.dp,
                            bottomEnd = if (message.sender == userID) 8.dp else 8.dp
                        )
                    )
                    .padding(horizontal = 4.dp, vertical = 4.dp),
            ) {
                message.images.forEach {
                    AsyncImage(
                        model = it,
                        contentDescription = "image",
                        modifier = Modifier
                            .size(128.dp)
                            .weight(1f),
                        contentScale = ContentScale.Crop
                    )

                }
            }
        }

    }
}

data class Message(
    val sender: String = "",
    val receiver: String = "",
    val text: String = "",
    val timestamp: Timestamp = Timestamp(Date(System.currentTimeMillis())),
    val type: String = "text",
    val images: List<String> = emptyList()
)

fun timestampToDate(timestamp: Timestamp): String {
    try {
        val sdf = SimpleDateFormat("dd/MM/YYYY" , Locale.getDefault())
        val time = sdf.format(Date(System.currentTimeMillis()))
        /*val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val netDate = Date(timestamp)
        return sdf.format(netDate)*/
        return time

    } catch (e: Exception) {
        return e.localizedMessage ?: "error"
    }

}