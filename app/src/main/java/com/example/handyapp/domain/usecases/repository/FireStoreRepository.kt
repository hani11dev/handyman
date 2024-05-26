package com.example.handyapp.domain.usecases.repository

import Job
import android.net.Uri
import com.example.handyapp.Response
import com.example.handyapp.domain.model.Category
import com.example.handyapp.domain.model.Notification
import com.example.handyapp.home.chat.Message
import kotlinx.coroutines.flow.Flow

interface FireStoreRepository {
     fun getMessages(receiver : String) : Flow<Response<List<Message>>>
     fun sendMessage(message: Message, images: List<Uri>) : Flow<Response<Boolean>>
     fun getDeviceToken(id : String) : Flow<Response<String>>
     fun uploadMessageImages(id: String , images : List<Uri>) : Flow<Response<Boolean>>
     fun getCategories() : Flow<Response<List<Category>>>
     fun registerInfo(firstName: String, lastName: String, day: String, month: String, year: String, imageUri : Uri ,fileUri:Uri) : Flow<Response<Unit>>
     fun updateFinalRegisterInfo(about : String , workingAreas : String , averageSalary : Double , city : String , wilaya : String , street : String , lat : String , long: String) : Flow<Response<Unit>>
     fun getHandyManSettingsInfo() : Flow<Response<HashMap<String , String>>>
     fun getSavedJobs() : Flow<Response<List<Job>>>
     fun saveJob(jobID : String) : Flow<Response<Unit>>
     fun removeJob(jobID : String) : Flow<Response<Unit>>
     fun sendNotificationFireStore(notification: Notification) : Flow<Response<Unit>>
     fun getNotificationFireStore() : Flow<Response<List<Notification>>>
}