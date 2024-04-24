package com.example.handyapp.home.myTasks

import HeaderRow
import Task
import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.handyapp.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import toTask


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReportScreen(taskID:String,navController: NavHostController) {
    var progressState by rememberSaveable { mutableStateOf(false) }
    var ProblemSupportingText by rememberSaveable { mutableStateOf("") }
    var Problem by rememberSaveable { mutableStateOf("") }
    var ProblemError by rememberSaveable { mutableStateOf(false) }
    var TitleSupportingText by rememberSaveable { mutableStateOf("") }
    var Title by rememberSaveable { mutableStateOf("") }
    var TitleError by rememberSaveable { mutableStateOf(false) }
    /*val taskdoc = Task("", "", "", "", "", "", "", 1, "", "", "")
    var task by remember { mutableStateOf<Task>(taskdoc) }
    val tasksCollectionRef = Firebase.firestore.collection("tasks")
    LaunchedEffect(key1 = Unit) {
        //var taskdocument=tasksCollectionRef.document(taskID).get().await()
        //task= taskdocument.data?.toTask()!!
        tasksCollectionRef.document(taskID)
            .addSnapshotListener { querySnapshot, _ ->
                GlobalScope.launch {
                    querySnapshot?.let {
                        task = querySnapshot.data?.toTask()!!

                    }
                }

            }
    }*/

    var selectedImageUris by remember {
        mutableStateOf<List<Uri>>(emptyList())
    }
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris -> selectedImageUris = uris })

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    )
    { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .background(
                    color =  MaterialTheme.colorScheme.background
                )
        ) {
            HeaderRow(/*navController = navController,*/ title = "Report Problem"/*, onClick = {}*/)
            Text(
                text = "Title :",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)
            )
            OutlinedTextField(
                value = Title,
                onValueChange = {
                    Title = it
                    TitleSupportingText = ""
                },
                label = { Text("Title") },
                isError = TitleError,
                singleLine = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Problem :",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)
            )
            OutlinedTextField(
                value = Problem,
                onValueChange = {
                    Problem = it
                    ProblemSupportingText = ""
                },
                label = { Text("What went wrong?") },
                isError = ProblemError,
                singleLine = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
            /*Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = {
                            multiplePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    ) {
                        Text(text = "Upload")
                    }
                }
                selectedImageUris.forEach { uri ->
                    Row(
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }*/

            Button(
                onClick = {
                    ProblemError = false
                    TitleError=false
                    if (Problem.isEmpty()||Title.isEmpty()) {
                        progressState=false
                        if(Problem.isEmpty()){
                            ProblemError = true
                            ProblemSupportingText = "Problem description can't be empty"
                        }
                        if(Title.isEmpty()){
                            TitleError=true
                            TitleSupportingText="Title can't be empty"
                        }
                    } else {
                        Firebase.firestore.collection("Reports")
                            .add(
                                hashMapOf(
                                    "TaskID" to taskID,
                                    "Title" to Title,
                                    "Description" to Problem,
                                    "Sender" to "Handyman",
                                )
                            )
                            .addOnSuccessListener { documentReference ->
                                println("DocumentSnapshot added with ID: ${documentReference.id}")
                            }
                            .addOnFailureListener { e ->
                                println("Error adding document: $e")
                            }
                    }
                }, modifier = Modifier.align(Alignment.End).padding(10.dp)
            ) {
                Text(text = "Submit")
            }
        }
    }
}
