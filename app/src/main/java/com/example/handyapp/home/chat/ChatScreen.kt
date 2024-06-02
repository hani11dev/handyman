package com.example.handyapp.home.chat

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
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
            if (uris.isNotEmpty()){
                selectedImageUris = uris
                viewModel.sendMessage(
                    Message(
                        sender = viewModel.userID.value!!,
                        ClientID,
                        text = messageText,
                        timestamp = Timestamp(Date(System.currentTimeMillis())),
                        type = "images"
                    ),
                    images = selectedImageUris
                )

            }
        })
    when (val resp = viewModel.deviceToken.value) {
        is Response.onLoading -> {}
        is Response.onFaillure -> {}
        is Response.onSuccess -> {
            deviceToken = resp.data
        }

    }

    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()




    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = if (isSystemInDarkTheme()) R.drawable.chat_back_dark else R.drawable.chat_back),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()

        ) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.BottomEnd) {


                when (val resp = viewModel.messages.value) {
                    is Response.onLoading -> {}
                    is Response.onFaillure -> {}
                    is Response.onSuccess -> {
                        if (resp.data.isNotEmpty()) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    //.weight(1f)
                                    .padding(horizontal = 12.dp),
                                verticalArrangement = Arrangement.Bottom,
                                reverseLayout = true,
                                state = state
                            ) {

                                items(resp.data.sortedByDescending { it.timestamp }) {
                                    ChatItem(message = it, viewModel.userID.value!!)
                                }
                            }

                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                //.weight(1f),
                                , contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.message_illust),
                                    contentDescription = null
                                )
                            }
                        }

                    }
                }
                val showScroll by remember {
                    derivedStateOf {
                        when (state.firstVisibleItemIndex) {
                            0, 1 -> {
                                false
                            }

                            else -> {
                                true
                            }
                        }
                    }
                }
                Column {
                    AnimatedVisibility(visible = showScroll) {
                        FloatingActionButton(
                            onClick = {
                                scope.launch {
                                    state.animateScrollToItem(0)
                                }
                            },
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowDownward,
                                contentDescription = null
                            )
                        }

                    }

                }


            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 8.dp, horizontal = 8.dp),
            ) {
                OutlinedTextField(
                    value = messageText, onValueChange = {
                        messageText = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = {
                            if (messageText.isNotEmpty()) {


                                viewModel.sendMessage(
                                    Message(
                                        sender = viewModel.userID.value!!,
                                        ClientID,
                                        text = messageText,
                                        timestamp = Timestamp(Date(System.currentTimeMillis()))
                                    ),
                                    images = selectedImageUris
                                )
                                messageText = ""
                                sendNotification(deviceToken, "new message", messageText)
                            }

                        }) {

                            Icon(
                                imageVector = Icons.Filled.Send,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    placeholder = {
                        Text(
                            "Message...",
                            color = if (isSystemInDarkTheme()) Color.White else Color.Gray
                        )
                    },
                    leadingIcon = {
                        IconButton(onClick = {
                            multiplePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.gallery),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(30.dp)
                )

            }
        }
    }
}



