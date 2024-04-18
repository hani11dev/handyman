package com.example.handyapp.domain.usecases.repository

import android.net.Uri
import com.example.handyapp.Response
import com.example.handyapp.home.Message
import kotlinx.coroutines.flow.Flow

interface FireStoreRepository {
     fun getMessages(receiver : String) : Flow<Response<List<Message>>>
     fun sendMessage(message: Message, images: List<Uri>) : Flow<Response<Boolean>>
     fun getDeviceToken(id : String) : Flow<Response<String>>
     fun uploadMessageImages(id: String , images : List<Uri>) : Flow<Response<Boolean>>
}