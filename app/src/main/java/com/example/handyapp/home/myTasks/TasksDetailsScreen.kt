package com.example.handyapp.home.myTasks

import Clientinf
import HeaderRow
import Task
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.handyapp.R
import com.example.handyapp.navigation.Screen
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import updateStatus
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun taskDetailScreen(taskID:String,task:Task,client:Clientinf,navController: NavHostController, context: Context = LocalContext.current) {
    var showDoneConfirmation by remember { mutableStateOf(false) }
    var showReportConfirmation by remember { mutableStateOf(false) }
    var showCancelConfirmation by remember { mutableStateOf(false) }
    val dateformat= DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val timeformat= DateTimeFormatter.ofPattern("HH:mm")
    val currentDate= LocalDate.now()
    val formattedDate=currentDate.format(dateformat)
    val currenttime= LocalTime.now()
    //val formattedTime=currentTime.format(timeformat)/**/
    val tasksCollectionRef = Firebase.firestore.collection("tasks")
    var paused by remember { mutableStateOf(if (task.status == "Paused") true else false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
            .clip(RoundedCornerShape(15.dp))
            .background(
                color = MaterialTheme.colorScheme.background
            ),
    ) {
        HeaderRow(/*navController = navController,*/ title = "Task Details"/*, onClick = {}*/)
        ElevatedCard(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSecondary),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 2.dp, horizontal = 8.dp)
                .clip(RoundedCornerShape(8.dp)),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)) {
                /*AsyncImage(
                    model = bid.ClientImage,
                    contentDescription = "Client Image",
                    modifier = Modifier.clip(
                        RoundedCornerShape(8.dp)
                    ),
                    contentScale = ContentScale.Crop
                )*/
                Text(
                    text = client.first_name+" "+client.last_name,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .align(Alignment.CenterStart)
                )
                if(task.status!="Cancelled" && task.status!="Rejected" && task.status!="Done"){
                    Row(horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                        modifier=Modifier.align(Alignment.CenterEnd)
                    ){
                        IconButton(onClick = {
                            val uri = Uri.parse("tel:" +client.phoneNbr)
                            val intnet = Intent(Intent.ACTION_DIAL , uri)
                            try {
                                context.startActivity(intnet)
                            }catch (e:SecurityException){

                            }
                        }) {
                            Icon(imageVector = Icons.Filled.Phone, contentDescription = "call")
                        }
                        IconButton(onClick = { navController.navigate(Screen.ChatScreen.route + "/${task.client}") }) {
                            Icon(imageVector = Icons.Filled.Email, contentDescription = "chat")
                        }
                        IconButton(onClick = { navController.navigate(Screen.MapScreen.route + "/${task.latitude}" + "/${task.longitude}") }) {
                            Icon(painter = painterResource(R.drawable.loc), contentDescription = "location")
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Status ",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Text(
            text = task.status, textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Category ",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Text(
            text = task.category, textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "title", textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Text(
            text = task.title, textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Description ",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Text(
            text = task.description, textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Price ", textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Text(
            text = task.Price.toString() + " DA", textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Location ",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Row {
            Text(
                text = task.Willaya, textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            if(task.status!="Cancelled" && task.status!="Rejected"){/*Text(
                text = task.Address, textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(horizontal = 12.dp)
            )*/
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Time ", textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Row {
            Text(
                text = task.time_day, textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Text(
                text = task.time_hour, textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
        Spacer(
            modifier = Modifier
                .height(40.dp)
                .align(Alignment.End)
        )

        if(task.status=="In Progress"||task.status=="Paused") {
            Row(modifier = Modifier
                .padding(20.dp)
                .align(Alignment.End))
            {
                Column (verticalArrangement = Arrangement.Center){
                    OutlinedButton(
                        onClick = {
                            showDoneConfirmation=true
                        },
                        modifier = Modifier
                            .width(105.dp)
                            .padding(2.dp)
                    ) {
                        Text(
                            text = "Done",
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    OutlinedButton(
                        onClick = {
                            showReportConfirmation=true
                            //navController.navigate(Screen.ReportScreen.route + "/${taskID}")
                        },
                        modifier = Modifier
                            .width(105.dp)
                            .padding(2.dp)

                    ) {
                        Text(
                            text = "Report",
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                Column(verticalArrangement = Arrangement.Center){
                    OutlinedButton(
                        onClick = {
                            paused = !paused
                            if (paused) {
                                updateStatus(tasksCollectionRef, task, "Paused",navController)
                            } else {
                                updateStatus(tasksCollectionRef, task, "In Progress",navController)
                            }
                        },
                        modifier = Modifier
                            .padding(2.dp)
                            .width(105.dp)
                        //.align(Alignment.End)
                    ) {
                        if (!paused) {
                            Text(
                                text = "Pause",
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text(
                                text = "Resume",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    OutlinedButton(
                        onClick = {
                            showCancelConfirmation=true
                            //updateStatus(tasksCollectionRef, task, "Cancelled",navController)
                        },
                        modifier = Modifier
                            .padding(2.dp)
                            .width(105.dp)
                        //.align(Alignment.End),

                    ) {
                        Text(
                            text = "Cancel",
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
            //if (task.time_day <= formattedDate) {}
        }
    }
    if(showDoneConfirmation){
        AlertDialog(
            onDismissRequest = { showDoneConfirmation = false },
            title = { Text("Done Confirmation") },
            text = { Text("Do you done this task successfully?") },
            confirmButton = {
                Button(
                    onClick = {

                        showDoneConfirmation= false // Dismiss the dialog
                    }
                ) {
                    Text("Done")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDoneConfirmation = false }
                ) {
                    Text("Undo")
                }
            }
        )
    }
    if(showReportConfirmation){
        AlertDialog(
            onDismissRequest = { showReportConfirmation = false },
            title = { Text("Report Client") },
            text = {
                Column {
                    Text("Something went wrong?")
                    Text("Do you want to the client?")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        navController.navigate(Screen.ReportScreen.route + "/${taskID}")
                        showReportConfirmation= false // Dismiss the dialog
                    }
                ) {
                    Text("Report")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showReportConfirmation = false }
                ) {
                    Text("Undo")
                }
            }
        )
    }
    if(showCancelConfirmation){
        AlertDialog(
            onDismissRequest = { showCancelConfirmation = false },
            title = { Text("Cancel Task") },
            text = {
                Text("Are sure you want to cancel this task?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        updateStatus(tasksCollectionRef, task, "Cancelled",navController)
                        showCancelConfirmation= false // Dismiss the dialog
                    }
                ) {
                    Text("Cancel")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showCancelConfirmation = false }
                ) {
                    Text("Undo")
                }
            }
        )
    }
}