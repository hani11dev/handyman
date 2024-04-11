package com.example.handyapp.home.myRequests

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
import java.io.IOException
import java.lang.reflect.Modifier
import java.net.HttpURLConnection
import java.net.URL



data class request(
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


@Composable
fun requestPictureDisplay(requestID: String, onSuccess: (List<String>) -> Unit) {
    val db = Firebase.firestore
    val scope = rememberCoroutineScope() // Use viewModelScope if using ViewModels

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



/*@Composable
fun DetailScreen(navController: NavController, requestID: String) {

    // Show a toast with requestID
    Toast.makeText(
        LocalContext.current,
        "Request ID: $requestID",
        Toast.LENGTH_SHORT
    ).show()


}*/
@Composable
fun DetailScreen(navController: NavController, requestID: String) {
    val imagesListState = remember { mutableStateListOf<String>() }

    // Call the requestPictureDisplay function to load images
    requestPictureDisplay(requestID) { imagesList ->
        imagesListState.clear()
        imagesListState.addAll(imagesList)
    }

    Column {
        Text(text = "Request Details")

        // Display the images using Image composable inside a LazyColumn
        LazyRow {
            items(imagesListState) { imageUrl ->
                Image(
                    painter = rememberImagePainter(imageUrl),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}











