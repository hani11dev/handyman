package com.example.handyapp.home.Settings.data

import com.example.handyapp.Response
import com.example.handyapp.home.Settings.AuthRepo
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepoImpl @Inject constructor (val auth: FirebaseAuth)  : AuthRepo {
    override fun signOut(): Flow<Response<Boolean>> = flow {
        try {
            emit(Response.onLoading)
            auth.signOut()
            emit(Response.onSuccess(true))

        } catch (e: Exception) {
            emit(Response.onFaillure(e.localizedMessage ?: "An enexpected error"))
        }
    }
}