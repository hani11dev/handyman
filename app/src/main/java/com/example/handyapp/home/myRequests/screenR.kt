package com.example.handyapp.home.myRequests



import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.handyapp.R
import com.example.handyapp.navigation.Screen
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


val taskCollectionRef = Firebase.firestore.collection("tasks")

@Composable
fun MyRequestsScreenReal(
    context: Context,
    clientRef: CollectionReference,
    request: Map<String, Any>,
    navController: NavHostController,
) {
    val backgroundColor = MaterialTheme.colorScheme.surface
    val title = request["title"] as? String ?: ""
    val name = remember { mutableStateOf<String?>(null) }
    val wilaya = request["wilaya"] as? String ?: ""
    val city = request["city"] as? String ?: ""
    val budget = request["budget"].toString() // Convert budget to string
    val day = request["day"] as? String ?: ""
    val description = request["description"] as? String ?: ""
    val hour = request["hour"] as? String ?: ""
    val street = request["street"] as? String ?: ""
    val requestID = request["requestID"] as? String ?: ""

    var showAcceptConfirmation by remember { mutableStateOf(false) }
    var showRejectConfirmation by remember { mutableStateOf(false) }


    LaunchedEffect(key1 = Unit) {

        name.value = getClientFirstName(
            clientRef,
            request["clientId"] as? String ?: ""
        )
    }



    Card(
        modifier = Modifier.padding(4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(backgroundColor),
        border = BorderStroke(2.dp, Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Client: ${name.value ?: ""}",
                    modifier = Modifier.weight(1f)

                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "Location: $wilaya, $city",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Price: $budget",
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "Date: $day",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        showAcceptConfirmation = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF84D588)
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Accept",
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                Button(
                    onClick = {
                        showRejectConfirmation = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFDB6161)
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Decline",
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                ClickableText(
                    text = AnnotatedString.Builder().apply {
                        withStyle(
                            style = SpanStyle(
                                textDecoration =
                                TextDecoration.Underline, color = Color.Gray
                            )
                        ) {
                            append("Detail")
                        }

                    }.toAnnotatedString(),
                    onClick = {

                        //  navController.navigate(Screen.DetailRequest.route)
                        navController.navigate(Screen.DetailRequest.route + "/$requestID")
                    },
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

            }


            if (showAcceptConfirmation) {
                AlertDialog(
                    onDismissRequest = { showAcceptConfirmation = false },
                    title = { Text("Accept Request") },
                    text = { Text("Are you sure you want to accept this request?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                saveTask(
                                    taskCollectionRef,
                                    id = "1",
                                    client = name.value ?: "",
                                    category = "?",
                                    title = title,
                                    description = description,
                                    time_day = day,
                                    time_hour = hour,
                                    price = budget.toInt() ?: 0,
                                    localisation = "$wilaya,$city",
                                    status = "IN_PROGRESS"
                                )
                                deleteRequest(
                                    Title = title,
                                    Description = description,
                                    Wilaya = wilaya,
                                    City = city,
                                    Street = street,
                                    Day = day,
                                    Hour = hour,
                                    Budget = budget.toInt() ?: 0,
                                    clientId = request["clientId"] as? String ?: "",
                                    handymanID = request["handymanID"] as? String ?: ""
                                )
                                showAcceptConfirmation = false // Dismiss the dialog
                            }
                        ) {
                            Text("Accept")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { showAcceptConfirmation = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }

            if (showRejectConfirmation) {
                var rejectionReason by remember { mutableStateOf("") } // State for rejection reason

                AlertDialog(
                    onDismissRequest = { showRejectConfirmation = false },
                    title = { Text("Reject Request") },
                    text = {
                        Column {
                            Text("Are you sure you want to reject this request?")
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                value = rejectionReason,
                                onValueChange = { rejectionReason = it },
                                label = { Text("Rejection Reason") }
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                saveTask(
                                    taskCollectionRef,
                                    id = "0",
                                    client = name.value ?: "",
                                    category = "CATEGORY",
                                    title = title,
                                    description = description,
                                    time_day = day,
                                    time_hour = hour,
                                    price = budget.toInt() ?: 0,
                                    localisation = "$wilaya,$city",
                                    status = "REJECTED",
                                    rejection_Reason = rejectionReason // Save rejection reason
                                )
                                deleteRequest(
                                    Title = title,
                                    Description = description,
                                    Wilaya = wilaya,
                                    City = city,
                                    Street = street,
                                    Day = day,
                                    Hour = hour,
                                    Budget = budget.toInt() ?: 0,
                                    clientId = request["clientId"] as? String ?: "",
                                    handymanID = request["handymanID"] as? String ?: ""
                                )
                                showRejectConfirmation = false // Dismiss the dialog
                            }
                        ) {
                            Text("Reject")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { showRejectConfirmation = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }

    }
}


