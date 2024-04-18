package com.example.handyapp.home

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.handyapp.R
import com.example.handyapp.Response
import com.example.handyapp.common.sendNotification
import com.example.handyapp.home.chat.ChatViewModel
import com.example.handyapp.navigation.Screen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun ChatScreen(ClientID : String,
    viewModel: ChatViewModel = hiltViewModel(),
    context: Context = LocalContext.current
) {
    var messageText by rememberSaveable {
        mutableStateOf("")
    }
    var deviceToken by rememberSaveable {
        mutableStateOf("")
    }
    var selectedImageUris by remember {
        mutableStateOf<List<Uri>>(emptyList())
    }
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris ->
            selectedImageUris = uris
            viewModel.sendMessage(
                Message(
                    sender = viewModel.userID.value!!,
                    "fbLnjdn6cKgkZTEmOd3hIqI9VYm1",
                    text = messageText,
                    timestamp = System.currentTimeMillis().toString(),
                    type = "images"
                ),
                images = selectedImageUris
            )
        })
    when (val resp = viewModel.deviceToken.value) {
        is Response.onLoading -> {}
        is Response.onFaillure -> {}
        is Response.onSuccess -> {
            deviceToken = resp.data
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        when (val resp = viewModel.messages.value) {
            is Response.onLoading -> {}
            is Response.onFaillure -> {}
            is Response.onSuccess -> {
                if (resp.data.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.Bottom,
                        reverseLayout = true,
                    ) {
                        items(resp.data.sortedByDescending { it.timestamp }) {
                            ChatItem(message = it, viewModel.userID.value!!)
                        }
                    }

                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.message_illust),
                            contentDescription = null
                        )
                    }
                }

            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        ) {
            TextField(
                value = messageText, onValueChange = {
                    messageText = it
                },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = {
                        viewModel.sendMessage(
                            Message(
                                sender = viewModel.userID.value!!,
                                ClientID,
                                text = messageText,
                                timestamp = System.currentTimeMillis().toString()
                            ),
                            images = selectedImageUris
                        )
                        messageText = ""
                        sendNotification(deviceToken, "new message", messageText)

                    }) {

                        Icon(
                            imageVector = Icons.Filled.Send,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                placeholder = { Text("Message") },
                leadingIcon = {
                    IconButton(onClick = {
                        multiplePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.AccountBox,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )

        }
    }
}

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
                    Text(text = timestampToDate(message.timestamp))
                }
                Text(
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
                    .wrapContentSize()
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
    val timestamp: String = "1713304421",
    val type: String = "text",
    val images: List<String> = emptyList()
)

fun timestampToDate(timestamp: String): String {
    try {
        val sdf = SimpleDateFormat("DD/MM/yyyy", Locale.getDefault())
        val netDate = Date(timestamp.toLong() * 1000)
        return sdf.format(netDate)

    } catch (e: Exception) {
        return e.localizedMessage ?: "error"
    }

}