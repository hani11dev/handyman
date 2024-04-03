package com.example.handyapp.home.myRequests


import MyTasksScreen
import Task
import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.BottomAppBarDefaults.containerColor
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.handyapp.R
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import lists


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
    var isDetailVisible by remember { mutableStateOf(false) }
    var selectedImage by remember { mutableStateOf(-1) } // Initialize selectedImage


    LaunchedEffect(key1 = Unit) {
        name.value = getClientFirstName(
            clientRef,
            request["clientId"] as? String ?: ""
        )
    }

    Card(
        modifier = Modifier.padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(backgroundColor),
        border = BorderStroke(2.dp, Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Client: ${name.value ?: ""}",
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Location: $wilaya, $city",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Price: $budget",
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Date: $day",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        saveTask(
                            taskCollectionRef,
                            Id = 1 ,
                            Client = name.value ?: "",
                            Category = "?",
                            Title =  title,
                            Description = description ,
                            Time_day = day,
                            Time_hour = hour ,
                            Price = budget.toInt() ?: 0,
                            localisation = "$wilaya,$city",
                            Status = "IN_PROGRESS"

                        )

                        deleteRequest(
                            Title = title ,
                            Description = description ,
                            Wilaya= wilaya,
                            City= city,
                            Street = street ,
                            Day = day,
                            Hour = hour ,
                            Budget= budget.toInt() ?:0,
                            clientId =  request["clientId"] as? String ?: "",
                            handymanID = request["handymanID"] as? String ?: ""

                        )




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

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {

                        saveTask(
                            taskCollectionRef,
                            Id = 1 ,
                            Client = name.value ?: "",
                            Category = "CATEGORY",
                            Title =  title,
                            Description = description ,
                            Time_day = day,
                            Time_hour = hour ,
                            Price = budget.toInt() ?: 0,
                            localisation = wilaya+","+city,
                            Status = "REJECTED"

                        )

                        deleteRequest(
                            Title = title ,
                            Description = description ,
                            Wilaya= wilaya,
                            City= city,
                            Street = street ,
                            Day = day,
                            Hour = hour ,
                            Budget= budget.toInt() ?:0,
                            clientId =  request["clientId"] as? String ?: "",
                            handymanID = request["handymanID"] as? String ?: ""

                        )



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

                Spacer(modifier = Modifier.width(8.dp))

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

                        isDetailVisible = !isDetailVisible
                    },
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                if (isDetailVisible) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Column {
                            Text(
                                text = "Description",
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = description,
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                // Display images here
                                listOf(R.drawable.adam, R.drawable._1).forEachIndexed { index, image ->
                                    Image(
                                        painter = painterResource(id = image),
                                        contentDescription = "Image $index",
                                        modifier = Modifier
                                            .size(100.dp)
                                            .clickable {
                                                selectedImage = image // Set selected image
                                            }
                                    )
                                }
                            }

                            // Display selected image in a full-screen dialog
                            if (selectedImage != -1) {
                                Dialog(onDismissRequest = { selectedImage = -1 }) {
                                    Box(
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        Image(
                                            painter = painterResource(id = selectedImage),
                                            contentDescription = "Selected Image",
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }




}



