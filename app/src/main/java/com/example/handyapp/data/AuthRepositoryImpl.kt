package com.example.handyapp.data

import com.example.handyapp.Response
import com.example.handyapp.domain.usecases.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.onesignal.OneSignal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    val auth: FirebaseAuth,
    val firestore: FirebaseFirestore
) : AuthRepository {


    override fun getUserID(): String = auth.currentUser!!.uid.toString()
    override fun signUp(
        email: String,
        password: String,
        phoneNumber: String
    ): Flow<Response<Unit>> =
        flow {
            val Handyman = hashMapOf(
                "PhoneNumber" to phoneNumber,
                "Status" to "NEW",
                "Email" to email,
                "DeviceToken" to OneSignal.User.pushSubscription.id,
                "FirstName" to "",
                "LastName" to "",
                "Day" to "",
                "Month" to "",
                "Year" to "",
                "Rating" to 0.toDouble(),
                "nbReviews" to 0.toDouble(),
                "OrdersCompleted" to "0",
                "Category" to "",
                "About" to "",
                "WorkingAreas" to "",
                "SubCategory" to "",
                "ProfileImage" to "",
                "Latitude" to "",
                "Longitude" to "",
            )
            try {
                emit(Response.onLoading)
                val a = auth.createUserWithEmailAndPassword(email , password).await()
                firestore.collection("HandyMan")
                    .document(a.user!!.uid)
                    .set(Handyman).await()
                emit(Response.onSuccess(Unit))
            }catch (e : Exception){
                emit(Response.onFaillure(e.localizedMessage?:"Error"))
            }
        }

    override fun signIn(email: String, password: String): Flow<Response<Unit>> =
        flow {
            try {
                emit(Response.onLoading)
                val exist = firestore.collection("HandyMan").whereEqualTo("Email", email).get(Source.SERVER).await()
                if (exist.documents.isNotEmpty()){
                    val uid = auth.signInWithEmailAndPassword(email , password).await()
                    firestore.collection("HandyMan").document(uid.user!!.uid).update("DeviceToken" , OneSignal.User.pushSubscription.id).await()
                    emit(Response.onSuccess(Unit))
                }else{
                    emit(Response.onFaillure("no account with this email and password"))
                }

            }catch (e:Exception){
                emit(Response.onFaillure(e.localizedMessage?:"error"))
            }
        }
}