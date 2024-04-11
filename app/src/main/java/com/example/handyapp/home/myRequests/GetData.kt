package com.example.handyapp.home.myRequests

import ImageSection
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext



data class Request(
    var requestID: String = "",
    val category: String = "",
    val city: String = "",
    val day: String = "",
    val description: String = "",
    val hour: String = "",
    val budget: Int = 0,
    val street: String = "",
    val title: String = "",
    val clientId: String = "",
    val handymanID: String = "",
    val wilaya: String = "",
)

fun deleteRequest(
    Title: String,
    Description: String,
    Wilaya: String,
    City: String,
    Street: String,
    Day: String,
    Hour: String,
    Budget: Int,
    clientId: String,
    handymanID: String
) = CoroutineScope(Dispatchers.IO).launch {
    try {
        val requestQuery = Firebase.firestore.collection("requests")
            .whereEqualTo("clientId", clientId)
            .whereEqualTo("handymanID", handymanID)
            .whereEqualTo("title", Title)
            .whereEqualTo("description", Description)
            .whereEqualTo("wilaya", Wilaya)
            .whereEqualTo("city", City)
            .whereEqualTo("street", Street)
            .whereEqualTo("day", Day)
            .whereEqualTo("hour", Hour)
            .whereEqualTo("budget", Budget)
            .get()
            .await()

        for (document in requestQuery) {
            try {
                document.reference.delete().await()
            } catch (e: Exception) {
                // Handle exception if needed
            }
        }
    } catch (e: Exception) {
        // Handle exception if needed
    }
}

fun saveTask(
    taskCollectionRef: CollectionReference,
    id: String, // must be a string
    client: String,
    category: String,
    title: String,
    description: String,
    time_day: String,
    time_hour: String,
    price: Int,
    localisation: String,
    status: String,
    rejection_Reason: String? = null // Make rejection reason nullable
) {
    val task = hashMapOf(
        "id" to id,
        "client" to client,
        "Category" to category,
        "title" to title,
        "description" to description,
        "time_day" to time_day,
        "time_hour" to time_hour,
        "price" to price,
        "localisation" to localisation,
        "Status" to status
    )

    // Conditionally add rejection reason only if the status is "REJECTED"
    if (status == "REJECTED" && rejection_Reason != null) {
        task["RejectionReason"] = rejection_Reason
    }

    taskCollectionRef.add(task)
}


suspend fun getCollectionData(
    handymanRef: CollectionReference,
    referenceId: String,
    onUpdate: (List<Map<String, Any>>) -> Unit // Callback to update the list
) {
    try {
        handymanRef
            .whereEqualTo("handymanID", referenceId) // Filter by handymanID
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    error.printStackTrace()
                    // Handle error (log or show error message)
                    return@addSnapshotListener
                }

                if (querySnapshot != null) {
                    val results = querySnapshot.documents.map { it.data ?: emptyMap() } // Convert documents to data maps
                    val sortedResults = results.sortedByDescending { (it["budget"] as? Long) ?: -1 }
                    onUpdate(sortedResults) // Call the callback to update the list
                }
            }
    } catch (e: Exception) {
        e.printStackTrace()
        // Handle error (log or show error message)
    }
}



suspend fun getClientFirstName(
    clientRef: CollectionReference,
    clientId: String,
): String? {
    try {
        val documentSnapshot = clientRef.document(clientId).get().await()
        if (documentSnapshot.exists()) {
            val data = documentSnapshot.data
            if (data != null && data.containsKey("FirstName")) {
                val firstName = data["FirstName"] as String
                Log.d("ClientName", "Retrieved client name: $firstName")
                return firstName
            } else {
                // onError("First Name field doesn't exist")
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


/*@Composable
fun requestPictureDisplay(requestID: String, onSuccess: (List<String>) -> Unit) {
    val db = Firebase.firestore
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = requestID) {
        scope.launch {
            val downloadedImages = try {
                loadingImages(requestID)
            } catch (e: Exception) {
                Log.e("loadingImages", "Error loading images: ${e.message}", e)
                emptyList()
            }
            onSuccess(downloadedImages) // Update onSuccess callback
        }
    }
}*/

/*@Composable
fun requestPictureDisplay(requestID: String, onSuccess: (List<String>) -> Unit) {
    val db = Firebase.firestore
    val scope = rememberCoroutineScope() // Use viewModelScope if using ViewModels
    val errorMessage = remember { mutableStateOf<String?>(null) } // State for error message

    LaunchedEffect(key1 = requestID) {
        scope.launch {
            try {
                val downloadedImages = loadingImages(requestID)
                onSuccess(downloadedImages) // Update onSuccess callback
            } catch (e: Exception) {
                errorMessage.value = "Error loading images: ${e.message}"
            }
        }
    }

    errorMessage.value?.let { message ->
        // Display error message if not null
        Text(text = message, color = Color.Red)
    }
}*/



@Composable
fun requestPictureDisplay(requestID: String, onSuccess: (List<String>) -> Unit) {
    val db = Firebase.firestore

    LaunchedEffect(key1 = requestID) {
        val requestDocument = db.collection("requests").document(requestID)
            .get().await()
        if (requestDocument.exists()){
            val loadedImages = try {
                loadingImages(requestID)
            } catch (e: Exception) {
                Log.e("loadImages", "Error loading images: ${e.message}", e)
                emptyList()
            }
            onSuccess(loadedImages)
            Log.d("ImageURLs", "Image URLs: $loadedImages")
        }
    }
}

private suspend fun loadingImages(requestID: String): List<String> = withContext(Dispatchers.IO) {
    val storageRef = Firebase.storage.reference.child("Request/$requestID")
    val requestFolderRef = storageRef

    val images = mutableListOf<String>()
    try {
        val result = requestFolderRef.listAll().await()

        for (itemRef in result.items) {
            if (itemRef.path.endsWith(".jpg") || itemRef.path.endsWith(".png") || itemRef.path.endsWith(".jpeg")) {
                val imageUrl = itemRef.downloadUrl.await().toString()
                Log.d("loadingImages", "Image URL: $imageUrl")
                images.add(imageUrl)
            }
        }
    } catch (e: Exception) {
        Log.e("loadingImages", "Error loading images: ${e.message}", e)
    }
    return@withContext images
}

@Composable
fun DetailScreen(
    navController: NavController,
    requestID: String
) {
    val db = Firebase.firestore
    var request by remember { mutableStateOf<Request?>(null) }
    val images = remember { mutableStateListOf<String>() }
    var selectedImage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = requestID) {
        val requestDocument = db.collection("requests").document(requestID).get().await()
        if (requestDocument.exists()) {
            request = requestDocument.toObject(Request::class.java)
            images.clear()
            val loadedImages = try {
                loadImages(requestID)
            } catch (e: Exception) {
                Log.e("loadImages", "Error loading images: ${e.message}", e)
                emptyList()
            }
            images.addAll(loadedImages)
            Log.d("ImageURLs", "Image URLs: $loadedImages")
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        request?.let { request ->
            RequestDetailsPage(request = request, images = images, navController = navController, onImageSelected = { imageUrl ->
                selectedImage = imageUrl
            })
        } ?: run {
            Text(text = "Loading request details...")
        }
    }

    selectedImage?.let { imageUrl ->
        AlertDialog(
            onDismissRequest = { selectedImage = null },
            title = { Text(text = "Image Detail") },
            text = { Image(painter = rememberImagePainter(imageUrl), contentDescription = null) },
            confirmButton = {
                Button(
                    onClick = { selectedImage = null },
                ) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun RequestDetailsPage(
    request: Request,
    images: SnapshotStateList<String>,
    navController: NavController,
    onImageSelected: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(end = 12.dp)
    ) {
        RequestDetailsHeader(request = request)

        Spacer(modifier = Modifier.height(16.dp))

        ImageSection(images = images, onImageSelected = onImageSelected)

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun RequestDetailsHeader(request: Request) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 32.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = request.title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Description:",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = request.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Location: ${request.street}, ${request.city}, ${request.wilaya}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Date: ${request.day} ${request.hour}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Budget: $${request.budget}",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun ImageSection(images: List<String>, onImageSelected: (String) -> Unit) {
    Column {
        Text(
            text = "Images",
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )
        LazyRow(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        ) {
            items(images) { imageUrl ->
                Image(
                    painter = rememberImagePainter(imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .width(200.dp)
                        .height(200.dp)
                        .padding(end = 16.dp)
                        .clickable {
                            onImageSelected(imageUrl)
                        },
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

private suspend fun loadImages(requestID: String): List<String> = withContext(Dispatchers.IO) {
    val storageRef = Firebase.storage.reference.child("Request/$requestID")
    val requestFolderRef = storageRef

    val images = mutableListOf<String>()
    try {
        val result = requestFolderRef.listAll().await()

        for (itemRef in result.items) {
            if (itemRef.path.endsWith(".jpg") || itemRef.path.endsWith(".png") || itemRef.path.endsWith(".jpeg")) {
                val imageUrl = itemRef.downloadUrl.await().toString()
                images.add(imageUrl)
            }
        }
    } catch (e: Exception) {
        Log.e("loadImages", "Error loading images: ${e.message}", e)
    }
    return@withContext images
}