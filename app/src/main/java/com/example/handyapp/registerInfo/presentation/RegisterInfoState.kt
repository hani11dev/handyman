package com.example.handyapp.registerInfo.presentation

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

data class RegisterInfoState (
    val firstName: String = "",
    val firstNameError: String? = null,
    val lastName: String = "",
    val lastNameError: String? = null,
    val day: String = "",
    val dayError: String? = null,
    val month: String = "",
    val monthError: String? = null,
    val year: String = "",
    val yearError: String? = null
)