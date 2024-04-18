package com.example.handyapp.data

import android.util.Log
import androidx.compose.runtime.rememberCoroutineScope
import com.example.handyapp.domain.usecases.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.onesignal.OneSignal
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.Exception

class AuthRepositoryImpl @Inject constructor(
    val auth: FirebaseAuth,
    val firestore: FirebaseFirestore
) : AuthRepository {


    override fun getUserID(): String = auth.currentUser!!.uid.toString()
}