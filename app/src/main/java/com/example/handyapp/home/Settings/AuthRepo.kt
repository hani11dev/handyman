package com.example.handyapp.home.Settings

import com.example.handyapp.Response
import kotlinx.coroutines.flow.Flow

interface AuthRepo{
    fun signOut() : Flow<Response<Boolean>>
}