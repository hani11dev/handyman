package com.example.handyapp.home.jobs

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.handyapp.Response
import com.example.handyapp.domain.usecases.removeSavedJobUseCase
import com.example.handyapp.domain.usecases.saveJobUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobItemViewModel @Inject constructor(private val saveJobUseCase: saveJobUseCase ,private val removeSavedJobUseCase: removeSavedJobUseCase) : ViewModel() {
    private var _saveState = mutableStateOf<Response<Unit>>(Response.onLoading)
    var saveState : State<Response<Unit>> = _saveState

    private var _removeState = mutableStateOf<Response<Unit>>(Response.onLoading)
    var removeState : State<Response<Unit>> = _removeState

    fun saveJob(jobID : String){
        viewModelScope.launch {
            saveJobUseCase(jobID).collect{
                _saveState.value = it
            }
        }
    }
    fun removeJob(jobID : String){
        viewModelScope.launch {
            removeSavedJobUseCase(jobID).collect{
                _removeState.value = it
            }
        }
    }
}