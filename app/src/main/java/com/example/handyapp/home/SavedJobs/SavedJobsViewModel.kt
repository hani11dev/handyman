package com.example.handyapp.home.SavedJobs

import Job
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.handyapp.Response
import com.example.handyapp.domain.usecases.getSavedJobsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedJobsViewModel @Inject constructor(private val GetSavedJobs : getSavedJobsUseCase) : ViewModel() {
    private var _savedJobs = mutableStateOf<Response<List<Job>>>(Response.onLoading)
    var savedJobs : State<Response<List<Job>>> = _savedJobs

    init {
        getSavedJobs()
    }

    fun getSavedJobs(){
        viewModelScope.launch {
            GetSavedJobs().collect{
                _savedJobs.value = it
            }
        }
    }

}