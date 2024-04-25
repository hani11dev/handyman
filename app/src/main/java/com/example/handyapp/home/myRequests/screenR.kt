package com.example.handyapp.home.myRequests



import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.handyapp.navigation.Screen
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


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
    val handymanID = request["handymanID"] as? String ?: ""
    val clientId = request["clientId"] as? String ?: ""

    var showAcceptConfirmation by remember { mutableStateOf(false) }
    var showRejectConfirmation by remember { mutableStateOf(false) }


    LaunchedEffect(key1 = Unit) {

        name.value = getClientFirstName(
            clientRef,
            request["clientId"] as? String ?: ""
        )
    }


    Surface( // Using Surface instead of Card for Material 3 styling
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface // Use surface color from theme
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Title with potential truncation
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 2, // Allow 2 lines for title
                overflow = TextOverflow.Ellipsis, // Ellipsis for truncation
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Stacked client and location info
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Client:",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.width(IntrinsicSize.Max) // Wrap content
                )
                Text(
                    text = name.value ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f) // Fill remaining space
                )
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Location:",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.width(IntrinsicSize.Max) // Wrap content
                )
                Text(
                    text = "$wilaya, $city",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f) // Fill remaining space
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Stacked price and date info
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Price:",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.width(IntrinsicSize.Max) // Wrap content
                )
                Text(
                    text = budget,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f) // Fill remaining space
                )
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Date:",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.width(IntrinsicSize.Max) // Wrap content
                )
                Text(
                    text = day,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f) // Fill remaining space
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button( // Updated to Material 3 Button
                    onClick = { showAcceptConfirmation = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Accept", color = Color.White) // White text for primary button
                }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton( // Updated to Material 3 OutlinedButton
                    onClick = { showRejectConfirmation = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Reject", color = MaterialTheme.colorScheme.error)
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton( // Updated to Material 3 TextButton
                    onClick = { navController.navigate(Screen.DetailRequest.route + "/$requestID") },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Detail", color = MaterialTheme.colorScheme.primary)
                }
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
                                    HandyId = handymanID,
                                    clientId = clientId,
                                    Category = "CATEGORY",
                                    Title = title,
                                    Description = description,
                                    Time_day = day,
                                    Time_hour = hour,
                                    Price = budget.toInt() ?: 0,
                                    Wilaya = wilaya,
                                    Address = "$street,$city",
                                    Status = "In_progress"
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
                                    clientId = clientId,
                                    handymanID = handymanID
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
                                    HandyId = handymanID,
                                    clientId = clientId,
                                    Category = "CATEGORY",
                                    Title = title,
                                    Description = description,
                                    Time_day = day,
                                    Time_hour = hour,
                                    Price = budget.toInt() ?: 0,
                                    Wilaya = wilaya,
                                    Address = "$street,$city",
                                    Status = "Rejected",
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