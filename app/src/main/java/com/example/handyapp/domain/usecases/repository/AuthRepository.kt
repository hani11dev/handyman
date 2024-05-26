package com.example.handyapp.domain.usecases.repository

import com.example.handyapp.Response
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
     fun getUserID() : String
     fun signUp(email : String , password : String , phoneNumber : String) : Flow<Response<Unit>>
     fun signIn(email: String , password: String) : Flow<Response<Unit>>
}