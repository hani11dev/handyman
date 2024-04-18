package com.example.handyapp.domain.usecases.repository

import com.example.handyapp.Response
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
     fun getUserID() : String
}