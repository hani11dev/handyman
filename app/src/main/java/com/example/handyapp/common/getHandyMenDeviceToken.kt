package com.example.handyapp.common

import com.example.handyapp.Response
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

suspend fun getHandyMenDeviceToken (id : String) : Flow<Response<String>> = flow {
    emit(Response.onLoading)
    val firestore = FirebaseFirestore.getInstance()
    try {
       val x = firestore.collection("HandyMen").document(id).get().await()
        if (x.exists()){
            x["DeviceToken"].let {
                emit(Response.onSuccess(it.toString()))
            }
        }else emit(Response.onFaillure("not exists"))

    }catch (e: Exception){
        emit(Response.onFaillure(e.localizedMessage?:"error"))
    }
}