package com.example.handyapp.home.myRequests

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class requestsDetailsViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var requestID : MutableState<String> = mutableStateOf("")
    init {
        savedStateHandle.get<String>("requestID")?.let {
            requestID.value = it
        }
    }



}