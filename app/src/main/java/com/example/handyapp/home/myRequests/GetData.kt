package com.example.handyapp.home.myRequests
import REQUEST
import Task
import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext





/*suspend fun getCollectionData(handymanRef: CollectionReference, referenceId: String): List<Map<String, Any>> {
    return try {
        val querySnapshot = withContext(Dispatchers.IO) {
            handymanRef
                .whereEqualTo("handymanID", referenceId) // Filter by handymanID
                .get()
                .await()
        }

        val results = querySnapshot.documents.map { it.data ?: emptyMap() } // Convert documents to data maps h
        val sortedResults = results.sortedByDescending { (it["budget"] as? Long) ?: -1 }


        sortedResults // Return sorted results
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList() // Return empty list in case of failure
    }
}*/

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



/*fun saveTask(taskRef: CollectionReference,
             Id:Int,
             Client:String,
             Category:String,
             Title:String,
             Description:String,
             Time_day: String,
             Time_hour: String,
             Price:Int,
             localisation:String,
             Status:String
) =
    CoroutineScope(Dispatchers.IO).launch {
        try{
            val task =Task(
                id = Id ,
                client = Client,
                category = Category,
                title =  Title,
                description = Description ,
                time_day = Time_day,
                time_hour =Time_hour ,
                Price = Price,
                localisation = localisation,
                status = Status
            )
            taskRef.add(task).await()
            withContext(Dispatchers.Main) {
                //  Toast.makeText(this@MainActivity, "Successfully saved ",
                //   Toast.LENGTH_LONG).show()
                
            }
        }catch(e : Exception){
            withContext(Dispatchers.Main) {
                //Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }*/


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
    Id: Int,
    Client: String,
    Category: String,
    Title: String,
    Description: String,
    Time_day: String,
    Time_hour: String,
    Price: Int,
    localisation: String,
    Status: String,
    RejectionReason: String? = null // Make rejection reason nullable
) {
    val task = hashMapOf(
        "Id" to Id,
        "Client" to Client,
        "Category" to Category,
        "Title" to Title,
        "Description" to Description,
        "Time_day" to Time_day,
        "Time_hour" to Time_hour,
        "Price" to Price,
        "localisation" to localisation,
        "Status" to Status
    )

    // Conditionally add rejection reason only if the status is "REJECTED"
    if (Status == "REJECTED" && RejectionReason != null) {
        task["RejectionReason"] = RejectionReason
    }

    taskCollectionRef.add(task)
}














