
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.handyapp.R
import com.example.handyapp.home.myTasks.taskDetailScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception


data class Task(
    val id: String,
    val client: String,
    val category: String,
    val title: String,
    val description: String,
    val time_day: String,
    val time_hour: String,
    val Price: Int,
    val Willaya: String,
    val Address:String,
    var status: String
)

data class Clientinf(
    val first_name:String,
    val last_name:String,
    val phoneNbr:String
)

@Composable
fun HeaderRow(
    //navController: NavHostController,
    title: String,
    //onClick: (navController: NavHostController) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(60.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier
                .padding(start = 16.dp),
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Taskcard(context : Context,task: Task,navController: NavHostController, onClick: (Int) -> Unit) {
    val tasksCollectionRef = Firebase.firestore.collection("tasks")
    var sheetstate = rememberModalBottomSheetState()
    var isSheetOpen by remember { mutableStateOf(false) }
    var showCancelConfirmation by remember { mutableStateOf(false) }
    var paused by remember { mutableStateOf(if (task.status == "Paused") true else false) }
    var cli=Clientinf("","","")
    var client by remember { mutableStateOf(cli) }
    var taskID by remember { mutableStateOf<String>("") }
    LaunchedEffect(key1 = Unit) {
        taskID=getTaskID(tasksCollectionRef,task)
    }
    LaunchedEffect(key1 = Unit) {
        var result=getClientInfo(Firebase.firestore.collection("Clients"),
            task.client,navController)
        if (result != null) {
            client= result
        }
    }
    ElevatedCard(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSecondary),
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            //.background(color =if(!paused) MaterialTheme.colorScheme. else  MaterialTheme.colorScheme. )
            .clickable {
                isSheetOpen = true
                // navController.navigate(Screen.TasksDetails.route + "/$taskID")//+"/${client.first_name+" "+client.last_name}"+"/${client.phoneNbr}")
            },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 12.dp, horizontal = 12.dp)
                    .align(Alignment.TopStart), verticalArrangement = Arrangement.Center
            )
            {
                Card(
                    modifier = Modifier
                        .wrapContentSize()
                        .weight(1f)
                    ,
                    colors = CardDefaults.cardColors(
                        containerColor = if (task.status == "Paused") colorResource(id = R.color.purple_200) else colorResource
                            (id = R.color.teal_700)
                    )
                ) {
                    Text(
                        text = " "+task.status+" ",
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                Text(
                    text = client.first_name+" "+client.last_name,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = task.title,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = task.Willaya,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Light
                )
                /*Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.location),
                        contentDescription = "location",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = task.Willaya,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                }*/
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = task.time_day,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Light
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = task.time_hour,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Light
                    )
                }
            }
            Column(
                modifier = Modifier
                    .padding(vertical = 12.dp, horizontal = 12.dp)
                    .align(Alignment.TopEnd),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append("Price ")
                    }
                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        append(text = task.Price.toString() + "DA")
                    }
                    withStyle(SpanStyle(fontWeight = FontWeight.Light)) {
                        append("/hr")
                    }
                })
                OutlinedButton(
                    onClick = {
                        showCancelConfirmation=true
                    },
                    modifier = Modifier.padding(2.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Red,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )

                ) {
                    Text(
                        text = "Cancel",
                        fontWeight = FontWeight.Bold,
                    )
                }
                OutlinedButton(
                    onClick = {
                        paused = !paused
                        if (paused) {
                            updateStatus(tasksCollectionRef, task, "Paused",navController)
                        } else {
                            updateStatus(tasksCollectionRef, task, "In Progress",navController)
                        }
                    },
                    modifier = Modifier.padding(2.dp)
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
            }
        }
    }
    if (isSheetOpen) {
        ModalBottomSheet(
            sheetState = sheetstate,
            onDismissRequest = {
                isSheetOpen = false
            }) {
            taskDetailScreen(taskID = taskID, task = task, client = client, navController = navController)
        }
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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Taskcardcanceld(task: Task,navController: NavHostController) {
    val tasksCollectionRef = Firebase.firestore.collection("tasks")
    var sheetstate = rememberModalBottomSheetState()
    var isSheetOpen by remember { mutableStateOf(false) }
    var cli=Clientinf("","","")
    var client by remember { mutableStateOf(cli) }
    LaunchedEffect(key1 = Unit) {
        var result=getClientInfo(Firebase.firestore.collection("Clients"),
            task.client,navController)
        if (result != null) {
            client= result
        }
    }
    var taskID by remember { mutableStateOf<String>("") }
    LaunchedEffect(key1 = Unit) {
        taskID=getTaskID(tasksCollectionRef,task)
    }
    ElevatedCard(
        colors =  CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSecondary),
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp)
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                       isSheetOpen=true
                // navController.navigate(Screen.TasksDetails.route + "/$taskID")//+"/${client.first_name+" "+client.last_name}"+"/${client.phoneNbr}")
            },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 12.dp, horizontal = 12.dp)
                    .align(Alignment.TopStart), verticalArrangement = Arrangement.Center
            )
            {

                Text(
                    text = client.first_name+" "+client.last_name,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = task.title,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = task.Willaya,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Light
                )

            }
            Column(
                modifier = Modifier
                    .padding(vertical = 12.dp, horizontal = 12.dp)
                    .align(Alignment.TopEnd),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .wrapContentSize()
                        .weight(1f)
                    ,
                    colors = CardDefaults.cardColors(
                        containerColor = if (task.status == "Cancelled") colorResource(id =R.color.yellow )
                        else if (task.status == "Rejected") colorResource(id =R.color.red_100)
                        else colorResource(id =R.color.green)
                    )
                ) {
                    Text(
                        text = " "+task.status+" ",
                        modifier = Modifier.padding(horizontal = 2.dp, vertical = 2.dp).align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                Text(text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append("Price ")
                    }
                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        append(text = task.Price.toString() + "DA")
                    }
                    withStyle(SpanStyle(fontWeight = FontWeight.Light)) {
                        append("/hr")
                    }
                })
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = task.time_day,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Light
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = task.time_hour,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Light
                    )
                }

            }
        }
    }
    if (isSheetOpen) {
        ModalBottomSheet(
            sheetState = sheetstate,
            onDismissRequest = {
                isSheetOpen = false
            }) {
            taskDetailScreen(taskID = taskID, task = task, client = client, navController = navController)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTasksScreen(navController: NavHostController , context: Context = LocalContext.current) {
    val tasksCollectionRef = Firebase.firestore.collection("tasks")
    val currentUser = FirebaseAuth.getInstance().currentUser
    var userId = ""
    if (currentUser != null) {
        userId = currentUser.uid
    }
    var tasklist by remember { mutableStateOf<List<Task>>(emptyList()) }
    val listenerRegistration by remember { mutableStateOf<ListenerRegistration?>(null) }
    LaunchedEffect(key1 = tasksCollectionRef) {
        val registration = getTask(tasksCollectionRef, userId) { updateDocument ->
            tasklist = updateDocument
        }
    }
    val statList = listOf("All", "Cancelled", "In Progress", "Done", "Rejected", "Paused")
    var isExpanded by remember { mutableStateOf(false) }
    var selectedStatus by remember { mutableStateOf(statList[0]) }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    )
    { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    color = MaterialTheme.colorScheme.background
                )
        ) {
            item {
                HeaderRow(/*navController = navController,*/ title = "Tasks"/*, onClick = {}*/)
            }
            item {
                ExposedDropdownMenuBox(
                    expanded = isExpanded,
                    onExpandedChange = { isExpanded = !isExpanded },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    TextField(
                        value = selectedStatus, onValueChange = {}, readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                        }, modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    DropdownMenu(
                        expanded = isExpanded, onDismissRequest = { isExpanded = false },
                        modifier = Modifier
                            .exposedDropdownSize()
                            .padding(2.dp)
                    ) {
                        DropdownMenuItem(
                            text = { Text(statList[0]) },
                            onClick = {
                                selectedStatus = statList[0]
                                isExpanded = false
                            }
                        )


                        DropdownMenuItem(
                            text = { Text(statList[1]) },
                            onClick = {
                                selectedStatus = statList[1]
                                isExpanded = false
                            }
                        )

                        DropdownMenuItem(
                            text = { Text(statList[2]) },
                            onClick = {
                                selectedStatus = statList[2]
                                isExpanded = false
                            }
                        )

                        DropdownMenuItem(
                            text = { Text(statList[3]) },
                            onClick = {
                                selectedStatus = statList[3]
                                isExpanded = false
                            }
                        )

                        DropdownMenuItem(
                            text = { Text(statList[4]) },
                            onClick = {

                                selectedStatus = statList[4]
                                isExpanded = false

                            }
                        )

                        DropdownMenuItem(
                            text = { Text(statList[5]) },
                            onClick = {
                                selectedStatus = statList[5]
                                isExpanded = false

                            }
                        )
                    }

                }
            }
            if (selectedStatus == "All") {
                items(tasklist) { item ->
                    if (item.status == "Done" || item.status == "Rejected" || item.status == "Cancelled") {
                        Taskcardcanceld(task = item , navController)
                    } else {
                        Taskcard(context = context,task = item , navController = navController  ) { cardId ->
                            updateStatus(tasksCollectionRef, item, "Cancelled",navController)
                        }
                    }
                }
            } else {
                items(tasklist) { item ->
                    if (selectedStatus == item.status) {
                        if (item.status == "Done" || item.status == "Rejected" || item.status == "Cancelled") {
                            Taskcardcanceld(task = item , navController)
                        } else {
                            Taskcard(context = context,task = item , navController) { cardId ->
                                updateStatus(tasksCollectionRef, item, "Cancelled",navController)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun Map<String, Any>.toTask(): Task {
    val id = this["HandyId"] as? String ?: ""// Handle potential missing key with default value
    val client = this["clientId"] as? String ?: ""
    val category = this["Category"] as? String ?: ""
    val title = this["Title"] as? String ?: ""
    val description = this["Description"] as? String ?: ""
    val time_hour = this["Time_hour"] as? String ?: ""
    val price = this["Price"].toString().toInt() // Convert Price to Int
    val time_day = this["Time_day"] as? String ?: ""
    val willaya = this["Wilaya"] as? String ?: ""
    val address= this["Address"] as? String ?: ""
    val status = this["Status"] as? String ?: ""    // ... similar logic for other Task properties ...
    return Task(
        id,
        client,
        category,
        title,
        description,
        time_day,
        time_hour,
        price,
        willaya,
        address,
        status
    )
}

fun getTask(
    taskref: CollectionReference,
    id: String,
    onUpdate: (List<Task>) -> Unit
): ListenerRegistration {
    return taskref.whereEqualTo("HandyId",id)
        .addSnapshotListener { querySnapshot, _ ->
            querySnapshot?.let {
                val documents = mutableListOf<Task>()
                //val doc=querySnapshot.documents[0].data?.toTask()
                for (document in it.documents) {
                    val yourdocument = document.data?.toTask()
                    yourdocument?.let { doc -> documents.add(doc) }
                }
                onUpdate(documents)
            }
        }

}


suspend fun getTaskID(ref: CollectionReference, item: Task): String {
    var id by mutableStateOf("")
    val querySnapshot=ref
        .whereEqualTo("HandyId", item.id)
        .whereEqualTo("Description", item.description)
        .whereEqualTo("Time_day", item.time_day)
        .whereEqualTo("Time_hour", item.time_hour)
        .whereEqualTo("Address", item.Address)
        .limit(1)
        .get()
        .await()
    if(!querySnapshot.isEmpty){
        val document= querySnapshot.documents[0]
        id=document.id
    }
    return id

}
fun updateStatus(ref: CollectionReference, item: Task, newValue: String,navController: NavHostController) {
    ref
        .whereEqualTo("clientId", item.client)
        .whereEqualTo("Time_day", item.time_day)
        .whereEqualTo("Time_hour", item.time_hour)
        .whereEqualTo("Address", item.Address)
        .get()
        .addOnSuccessListener { documents ->
            for (doc in documents) {
                doc.reference.update("Status", newValue)
                    .addOnSuccessListener { }
                    .addOnFailureListener {}
            }
        }
        .addOnFailureListener {}

    /*addOnCompleteListener {task->

        for(doc in task.result.documents){
            doc.reference.update("status",newValue)
        }*/
}

fun Map<String,Any>.toClientinfo(navController: NavHostController): Clientinf{
    val firstname = this["FirstName"] as? String ?: ""
    val lastname = this["LastName"] as? String ?: ""
    val phone = this["PhoneNumber"] as? String ?: ""
    return Clientinf(firstname,lastname,phone)
}
suspend fun getClientInfo(
    clientref: CollectionReference,
    id: String,
    navController: NavHostController
): Clientinf?{
    try {
        val documentSnapshot = clientref.document(id).get().await()
        if (documentSnapshot.exists()) {
            val data = documentSnapshot.data
            if (data != null) {
                val result = data.toClientinfo(navController)
                return result
            }
        } else {
            //onError("Document doesn't exist for the provided client ID")
        }
    } catch (e: Exception) {
        e.printStackTrace()
        // we use to have a context argument but i had to remove it
        //onError("Error fetching client name: ${e.message}")
    }
    return null
}









