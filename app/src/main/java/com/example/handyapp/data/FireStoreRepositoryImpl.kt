package com.example.handyapp.data

import android.net.Uri
import android.util.Log
import com.example.handyapp.Response
import com.example.handyapp.domain.usecases.repository.FireStoreRepository
import com.example.handyapp.home.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.getField
import com.google.firebase.firestore.toObject
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

    override fun getMessages(receiver : String): Flow<Response<List<Message>>> = callbackFlow {
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
                                    if (message?.type == "text") {
                                        messages.add(message)
                                    } else {
                                        val ref =
                                            storage.reference.child("Messages/${it.id}").listAll().await()
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
                                    }
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
            val doc = fireStore.collection("Messages").add(message).await()
            if (message.type == "images") {
                for (i in images.indices) {
                    storage.reference.child("Messages/${doc.id}/image${i + 1}.jpg")
                        .putFile(images[i]).await()
                }
            }
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


}



