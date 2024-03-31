package com.example.handyapp.home.myRequests


import android.content.Context
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
import androidx.navigation.NavController
import com.example.handyapp.R
import com.google.firebase.firestore.CollectionReference


@Composable
fun MyRequestsScreenReal(
    context: Context,
    clientRef: CollectionReference,
    request: Map<String, Any>,
    navController: NavController,
) {
    val backgroundColor = MaterialTheme.colorScheme.surface
    val title = request["title"] as? String ?: ""
    val name = remember { mutableStateOf<String?>(null) }
    val wilaya = request["wilaya"] as? String ?: ""
    val city = request["city"] as? String ?: ""
    val budget = request["budget"].toString() // Convert budget to string
    val day = request["day"] as? String ?: ""
    val description = request["description"] as? String ?: ""
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
                // style = MaterialTheme.typography.h6,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center // Center title text
            )

            Spacer(modifier = Modifier.height(8.dp)) // Add space between title and other texts

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Client: ${name.value ?: ""}",
                    //  style = MaterialTheme.typography.body2,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp)) // Add space between text elements

                Text(
                    text = "Location: $wilaya, $city",
                    // style = MaterialTheme.typography.body2,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp)) // Add space between text rows

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Price: $budget",
                    //  style = MaterialTheme.typography.body2,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp)) // Add space between text elements

                Text(
                    text = "Date: $day",
                    //  style = MaterialTheme.typography.body2,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp)) // Add space between texts and buttons

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { /* Handle accept */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF84D588)
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Accept",
                        // style = MaterialTheme.typography.button
                    )
                }

                Spacer(modifier = Modifier.width(8.dp)) // Add equal space between buttons

                Button(
                    onClick = { /* Handle decline */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFDB6161)
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Decline",
                        //  style = MaterialTheme.typography.button
                    )
                }

                Spacer(modifier = Modifier.width(8.dp)) // Add equal space between buttons

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



