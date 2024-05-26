package com.example.handyapp.home.notification

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.handyapp.Response
import com.example.handyapp.domain.model.Notification
import com.example.handyapp.domain.usecases.getNotificationFirestoreUseCase
import com.example.handyapp.domain.usecases.sendNotificationFirestoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val getNotificationFirestoreUseCase: getNotificationFirestoreUseCase,
    private val sendNotificationFirestoreUseCase: sendNotificationFirestoreUseCase
) : ViewModel() {
    
    private var _sendNotificationState = mutableStateOf<Response<Unit>>(Response.onLoading)
    var sendNotificationState : State<Response<Unit>> = _sendNotificationState

    private var _notificationList = mutableStateOf<Response<List<Notification>>>(Response.onLoading)
    var notificationList : State<Response<List<Notification>>> = _notificationList
    
    init {
        viewModelScope.launch { 
            getNotificationFirestoreUseCase().collect{
                _notificationList.value = it
            }
        }
    }
    fun sendNotificationFireStore(notification : Notification){
        viewModelScope.launch { 
            sendNotificationFirestoreUseCase(notification).collect{
                _sendNotificationState.value = it
            }
        }
    }
    
}