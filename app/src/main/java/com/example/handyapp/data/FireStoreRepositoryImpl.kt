package com.example.handyapp.data

import Job
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import com.example.handyapp.Response
import com.example.handyapp.domain.model.Category
import com.example.handyapp.domain.model.Notification
import com.example.handyapp.domain.usecases.repository.FireStoreRepository
import com.example.handyapp.home.chat.Message
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireStoreRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore, private val storage: FirebaseStorage,
    private val auth: FirebaseAuth
) : FireStoreRepository {

    override fun getMessages(receiver: String): Flow<Response<List<Message>>> = callbackFlow {
        trySend(Response.onLoading)
        val snapshot = fireStore.collection("Messages")
            .where(
                Filter.or(
                    Filter.and(
                        Filter.equalTo("sender", auth.currentUser!!.uid),
                        Filter.equalTo("receiver", receiver)
                    ),
                    Filter.and(
                        Filter.equalTo("sender", receiver),
                        Filter.equalTo("receiver", auth.currentUser!!.uid)
                    )

                )
            ).addSnapshotListener { value, error ->
                GlobalScope.launch {
                    var messages = arrayListOf<Message>()
                    val resp: Response<List<Message>> =
                        if (error == null) {
                            if (value != null) {
                                //messages = value.toObjects(Message::class.java)
                                value.documents.forEach {
                                    val message = it.toObject(Message::class.java)
                                    //if (message?.type == "text") {
                                    if (message != null)
                                        messages.add(message)
                                    /*} else {
                                        val ref =
                                            storage.reference.child("Messages/${it.id}").listAll()
                                                .await()
                                        var imagesList = ArrayList<String>()
                                        for (item in ref.items) {
                                            val uri = item.downloadUrl.await()
                                            imagesList.add(uri.toString())
                                        }
                                        if (message != null) {
                                            if (imagesList.isNotEmpty())
                                                Log.d("img", imagesList[0] ?: "vide")
                                            messages.add(
                                                Message(
                                                    message.sender,
                                                    message.receiver,
                                                    message.text,
                                                    message.timestamp,
                                                    message.type,
                                                    imagesList
                                                )
                                            )

                                        }
                                    }*/
                                }
                            }
                            Response.onSuccess(messages)
                        } else Response.onFaillure(error.localizedMessage ?: "error")

                    trySend(resp).isSuccess
                }
            }
        awaitClose {
            snapshot.remove()
        }


    }

    override fun sendMessage(message: Message, images: List<Uri>): Flow<Response<Boolean>> = flow {
        try {
            emit(Response.onLoading)

            if (message.type == "images") {
                val doc = fireStore.collection("Messages").add(message).await()
                for (i in images.indices) {
                    storage.reference.child("Messages/${doc.id}/image${i + 1}.jpg")
                        .putFile(images[i]).addOnSuccessListener {
                            val uri = it.storage.downloadUrl.addOnSuccessListener {

                                fireStore.collection("Messages").document(doc.id)
                                    .update("images", FieldValue.arrayUnion(it))
                            }
                        }
                }
            } else fireStore.collection("Messages").add(message).await()
            emit(Response.onSuccess(true))
        } catch (e: Exception) {
            emit(Response.onFaillure(e.localizedMessage ?: "error"))
        }

    }

    override fun getDeviceToken(id: String): Flow<Response<String>> = flow {
        emit(Response.onLoading)
        try {
            val client = fireStore.collection("Clients").document(id).get().await()
            if (client.exists()) {
                val token = client.get("DeviceToken").toString()
                emit(Response.onSuccess(token))
            }
        } catch (e: Exception) {
            emit(Response.onFaillure(e.localizedMessage ?: "error"))
        }
    }

    override fun uploadMessageImages(id: String, images: List<Uri>): Flow<Response<Boolean>> =
        callbackFlow {
            Response.onLoading
            for (i in images.indices) {
                storage.reference.child("Messages/$id/image${i + 1}.jpg")
                    .putFile(images[i]).await()
            }
        }


    override fun getCategories(): Flow<Response<List<Category>>> = callbackFlow {
        Response.onLoading
        val snapshotListener =
            fireStore.collection("services").addSnapshotListener { value, error ->
                val resp = if (value != null) {
                    val categoryList = value.toObjects(Category::class.java)
                    Response.onSuccess<List<Category>>(categoryList)
                } else Response.onFaillure(error?.message ?: "unknown error")
                trySend(resp).isSuccess
            }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun registerInfo(
        firstName: String,
        lastName: String,
        day: String,
        month: String,
        year: String,
        imageUri: Uri,
        fileUri : Uri

    ): Flow<Response<Unit>> =
        flow {
            emit(Response.onLoading)
            try {
                fireStore.collection("HandyMan").document(auth.currentUser!!.uid).update(
                    "FirstName",
                    firstName,
                    "LastName",
                    lastName,
                    "Day",
                    day,
                    "Month",
                    month,
                    "Year",
                    year
                ).await()
                storage.reference.child("certificates/${auth.currentUser!!.uid}").putFile(fileUri).await()
                storage.reference.child("profile_pictures/profileImage").putFile(imageUri).addOnSuccessListener {
                    val uri = it.storage.downloadUrl.addOnSuccessListener {
                        fireStore.collection("HandyMan").document(auth.currentUser!!.uid)
                            .update("ProfileImage", it , "Status" , "WAITING")
                    }
                }.await()


                emit(Response.onSuccess(Unit))

            }catch (e : Exception){
                emit(Response.onFaillure(e.localizedMessage?:"error"))
            }
        }

    override fun updateFinalRegisterInfo(about : String , workingAreas : String , averageSalary : Double , city : String , wilaya : String , street : String , lat : String , long: String): Flow<Response<Unit>> =
        flow{
            try {
                emit(Response.onLoading)
                fireStore.collection("HandyMan").document(auth.currentUser!!.uid).update(
                    "About" , about,
                    "WorkingAreas" , workingAreas,
                    "AverageSalary" , averageSalary,
                    "City" , city,
                    "Wilaya" , wilaya,
                    "Street" , street,
                    "Latitude" , lat,
                    "Longitude" , long,
                    "Status" , "ACTIVE"
                ).await()
                emit(Response.onSuccess(Unit))

            }catch (e:Exception){
                emit(Response.onFaillure(e.localizedMessage?:"error"))
            }
    }

    override fun getHandyManSettingsInfo(): Flow<Response<HashMap<String, String>>> =
        callbackFlow {
            Response.onLoading
            val snap = fireStore.collection("HandyMan").document(auth.currentUser!!.uid).addSnapshotListener { value, error ->
                val resp : Response<HashMap<String , String>> =
                if (error == null){
                    if (value != null){
                        val profileImage = value.getString("ProfileImage")?:""
                        val email = value.getString("Email")?:""
                        val firstName = value.getString("FirstName")?:""
                        val lastName = value.getString("LastName")?:""
                        Response.onSuccess(hashMapOf("ProfileImage" to profileImage , "Email" to email , "FirstName" to firstName ,"LastName" to lastName))
                    }else{
                        Response.onFaillure("error")
                    }
                }else{
                    Response.onFaillure(error.localizedMessage?:"error")
                }
                trySend(resp).isSuccess
            }
            awaitClose {
                snap.remove()
            }
        }

    override fun getSavedJobs(): Flow<Response<List<Job>>>  = flow{
        emit(Response.onLoading)
        try {
            val doc = fireStore.collection("HandyMan").document(auth.currentUser!!.uid).get().await()
            val resp: Response<List<Job>> =
                if (doc.exists()) {
                    var savedHandyMan: ArrayList<Job> = arrayListOf()
                    val savedHandyManUriList = doc.get("SavedJobs") as List<String>
                    savedHandyManUriList.forEach {
                        val jobs = fireStore.collection("Jobs").document(it).get().await()
                        val job = jobs.toObject(Job::class.java)
                        savedHandyMan.add(
                            Job(
                                id = it,
                                category = job?.category ?:"",
                                city = job?.city?:"",
                                day = job?.day?:"",
                                description = job?.description?:"",
                                hour = job?.hour?:"",
                                max = job?.max?:0,
                                min = job?.min?:0,
                                status = job?.status?:"",
                                street = job?.street?:"",
                                title = job?.title?:"",
                                userId = job?.userId?:"",
                                job?.wilya?:"",
                                saved = true,
                                addingDate = job?.addingDate?: Timestamp.now()
                            )
                        )
                    }
                    Response.onSuccess(savedHandyMan)
                } else Response.onFaillure("HandyMen doesn't exist")
            emit(resp)
        } catch (e: Exception) {
            emit(Response.onFaillure(e.localizedMessage ?: "error"))
        }
    }

    override fun saveJob(jobID: String): Flow<Response<Unit>> =
        flow{
            try {
                fireStore.collection("HandyMan").document(auth.currentUser!!.uid)
                    .update("SavedJobs", FieldValue.arrayUnion(jobID)).await()
                emit(Response.onSuccess(Unit))
            } catch (e: Exception) {
                emit(Response.onFaillure(e.localizedMessage?:"error"))
            }
    }

    override fun removeJob(jobID: String): Flow<Response<Unit>> =
        flow {
            try {
                fireStore.collection("HandyMan").document(auth.currentUser!!.uid)
                    .update("SavedJobs", FieldValue.arrayRemove(jobID)).await()
                emit(Response.onSuccess(Unit))
            } catch (e: Exception) {
                emit(Response.onFaillure(e.localizedMessage?:"error"))
            }
        }

    override fun sendNotificationFireStore(notification: Notification): Flow<Response<Unit>> =
        flow {
            try {
                emit(Response.onLoading)
                fireStore.collection("Clients").document(notification.receiver)
                    .collection("Notification").document().set(Notification(notification.title,notification.text , auth.currentUser!!.uid , notification.receiver , notification.deepLink , Timestamp.now())).await()
                emit(Response.onSuccess(Unit))
            } catch (e: Exception) {
                emit(Response.onFaillure(e.localizedMessage ?: "error"))
            }
        }

    override fun getNotificationFireStore(): Flow<Response<List<Notification>>> =
        callbackFlow {
            Response.onLoading
            val snapshot = fireStore.collection("HandyMan").document(auth.currentUser!!.uid)
                .collection("Notification").addSnapshotListener { value, error ->
                    val resp: Response<List<Notification>> =
                        if (error == null) {
                            if (value != null) {
                                val notifications = value.toObjects(Notification::class.java)
                                Response.onSuccess(notifications)
                            } else {
                                Response.onFaillure("No notifications")
                            }
                        } else {
                            Response.onFaillure(error.localizedMessage ?: "error")
                        }
                    trySend(resp).isSuccess
                }
            awaitClose { snapshot.remove() }

        }
}



