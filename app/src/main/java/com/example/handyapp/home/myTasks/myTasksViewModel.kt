package com.example.handyapp.home.myTasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.handyapp.domain.model.Notification
import com.example.handyapp.domain.usecases.sendNotificationFirestoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class myTasksViewModel @Inject constructor(private val sendNotificationFirestoreUseCase: sendNotificationFirestoreUseCase) : ViewModel() {

    fun sendNotificationToFireStore(notification : Notification){
        viewModelScope.launch {
            sendNotificationFirestoreUseCase(notification).collect{

            }
        }
    }
}