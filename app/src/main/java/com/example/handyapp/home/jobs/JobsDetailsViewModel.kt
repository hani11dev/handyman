package com.example.handyapp.home.jobs

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class JobsDetailsViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

     var jobID : MutableState<String> = mutableStateOf("")
    init {
        savedStateHandle.get<String>("jobID")?.let {
            jobID.value = it
        }
    }
}