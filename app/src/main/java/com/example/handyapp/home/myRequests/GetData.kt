package com.example.handyapp.home.myRequests
import Task
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


suspend fun getCollectionData(
    handymanRef: CollectionReference,
    referenceId: String,
): List<Map<String, Any>> {
    val result = mutableListOf<Map<String, Any>>()
    try {
        val querySnapshot =
            handymanRef.whereEqualTo("handymanID", referenceId).get().await()
        for (document in querySnapshot.documents) {
            val data = document.data
            if (data != null) {
                result.add(data)
            }
        }
    } catch (e: Exception) {
        // Handle error (log or show error message)
        withContext(Dispatchers.Main) {
            // displayToast(e.message ?: "Unknown error occurred")

        }
    }
    return result
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



fun saveTask(taskRef: CollectionReference,
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
    }








