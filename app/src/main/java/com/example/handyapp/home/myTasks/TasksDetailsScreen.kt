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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.handyapp.R
import com.example.handyapp.navigation.Screen
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import getClientInfo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import toClientinfo
import toTask
import updateStatus
import java.lang.Exception
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TasksDetailsScreen(taskID:String,ClientName:String,PhoneNumber:String,navController: NavHostController) {
    val taskdoc=Task("","","","","","","",1,"","","")
    var task by remember { mutableStateOf<Task>(taskdoc) }
    val tasksCollectionRef = Firebase.firestore.collection("tasks")
    val ClientsCollectionRef = Firebase.firestore.collection("Clients")
    val cli = Clientinf("", "", "")
    var client by remember {mutableStateOf(cli)}
     LaunchedEffect(key1 = Unit) {
        //var taskdocument=tasksCollectionRef.document(taskID).get().await()
        //task= taskdocument.data?.toTask()!!
        tasksCollectionRef.document(taskID)
            .addSnapshotListener { querySnapshot, _ ->
                GlobalScope.launch {
                    querySnapshot?.let {
                    task= querySnapshot.data?.toTask()!!

                } }

            }

    }

    TaskDetails(task = task,ClientName,PhoneNumber,navController)
}
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TaskDetails(task:Task,client:String,PhoneNumber:String,navController: NavHostController, context: Context = LocalContext.current) {
    val tasksCollectionRef = Firebase.firestore.collection("tasks")
    var paused by remember { mutableStateOf(if (task.status == "Paused") true else false) }
    val dateformat= DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val timeformat= DateTimeFormatter.ofPattern("HH:mm")
    val currentDate= LocalDate.now()
    val formattedDate=currentDate.format(dateformat)
    val currenttime= LocalTime.now()
    //val formattedTime=currentTime.format(timeformat)/**/
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    )
    { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
                .background(
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.background else colorResource(
                        id = R.color.white//R.color.lightGray
                    )
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    /*AsyncImage(
                        model = bid.ClientImage,
                        contentDescription = "Client Image",
                        modifier = Modifier.clip(
                            RoundedCornerShape(8.dp)
                        ),
                        contentScale = ContentScale.Crop
                    )*/
                    Text(
                        text = client,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    //if(task.status!="Cancelled" && task.status!="Rjected"){}
                    Row(horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                        modifier=Modifier.align(Alignment.Bottom)
                    ){
                        IconButton(onClick = {
                            val uri = Uri.parse("tel:" + PhoneNumber)
                            val intnet = Intent(Intent.ACTION_DIAL , uri)
                            try {
                                context.startActivity(intnet)
                            }catch (e:SecurityException){

                            }
                        }) {
                            Icon(imageVector = Icons.Filled.Phone, contentDescription = "chat")
                        }
                        IconButton(onClick = { navController.navigate(Screen.ChatScreen.route + "/${task.client}") }) {
                            Icon(imageVector = Icons.Filled.Email, contentDescription = "chat")
                        }
                    }
                }
                /*Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .wrapContentSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {}*/

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
            if(task.status!="Cancelled" && task.status!="Rjected"){
                Row {
                    Text(
                        text = task.Willaya, textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    /*Text(
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

            if(task.status=="In_Progress"||task.status=="Paused") {
                Row(modifier = Modifier
                    .padding(2.dp)
                    .align(Alignment.End))
                {
                    Column (){
                        OutlinedButton(
                            onClick = {},
                            modifier = Modifier
                                .padding(2.dp)
                                //.align(Alignment.CenterHorizontally),
                            ,colors = ButtonDefaults.outlinedButtonColors(
                                // containerColor = Color.Red,
                                contentColor = MaterialTheme.colorScheme.onSecondary
                            )
                        ) {
                            Text(
                                text = "Done",
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        OutlinedButton(
                            onClick = {
                                navController.navigate(Screen.ReportScreen.route + "/${task.client}")
                            },
                            modifier = Modifier
                                .padding(2.dp)
                                //.align(Alignment.CenterHorizontally),
                            ,colors = ButtonDefaults.outlinedButtonColors(
                                // containerColor = Color.Red,
                                contentColor = MaterialTheme.colorScheme.onSecondary
                            )
                        ) {
                            Text(
                                text = "Report",
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                    Column(){
                        OutlinedButton(
                            onClick = {
                                paused = !paused
                                if (paused) {
                                    updateStatus(tasksCollectionRef, task, "Paused",navController)
                                } else {
                                    updateStatus(tasksCollectionRef, task, "In_Progress",navController)
                                }
                            },
                            modifier = Modifier
                                .padding(2.dp)
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
                                updateStatus(tasksCollectionRef, task, "Cancelled",navController)
                            },
                            modifier = Modifier
                                .padding(2.dp)
                                //.align(Alignment.End),
                            ,colors = ButtonDefaults.outlinedButtonColors(
                                //containerColor = Color.Red,
                                contentColor = MaterialTheme.colorScheme.onSecondary
                            )

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
    }
}