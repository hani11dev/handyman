package com.example.handyapp.home.chat

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.handyapp.Response
import com.example.handyapp.domain.usecases.getDeviceTokenUseCase
import com.example.handyapp.domain.usecases.getMessagesUseCase
import com.example.handyapp.domain.usecases.getUserIDUseCase
import com.example.handyapp.domain.usecases.sendMessageUseCase
import com.example.handyapp.domain.usecases.uploadMessageImagesUseCase
import com.example.handyapp.home.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getUserIDUseCase: getUserIDUseCase,
    private val getMessagesUseCase: getMessagesUseCase,
    private val sendMessagesUseCase: sendMessageUseCase,
    private val getDeviceTokenUseCase: getDeviceTokenUseCase,
    private val uploadMessageImagesUseCase: uploadMessageImagesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var userID: MutableState<String?> = mutableStateOf(null)
    private val _messages: MutableState<Response<List<Message>>> =
        mutableStateOf(Response.onLoading)
    val messages: State<Response<List<Message>>> = _messages

    private val _sendState: MutableState<Response<Boolean>> = mutableStateOf(Response.onLoading)
    val sendState: State<Response<Boolean>> = _sendState

    private val _deviceToken: MutableState<Response<String>> = mutableStateOf(Response.onLoading)
    val deviceToken: State<Response<String>> = _deviceToken

    private val _uploadImageState: MutableState<Response<Boolean>> = mutableStateOf(Response.onLoading)
    val uploadImageState: State<Response<Boolean>> = _uploadImageState

    init {
        userID.value = getUserIDUseCase()
        savedStateHandle.get<String>("ClientID")?.let {
            viewModelScope.launch {
                getMessagesUseCase(it).collect {
                    _messages.value = it
                }
            }

        }

        viewModelScope.launch {
            getDeviceTokenUseCase(id = "fbLnjdn6cKgkZTEmOd3hIqI9VYm1").collect {
                _deviceToken.value = it
            }
        }

    }

    fun sendMessage(message: Message , images: List<Uri>) {
        viewModelScope.launch {
            sendMessagesUseCase(message , images).collect {
                _sendState.value = it
            }
        }
    }
    fun uploadMessageImage(id : String , images : List<Uri>){
        viewModelScope.launch {
            uploadMessageImagesUseCase(id , images).collect{
                _uploadImageState.value = it
            }
        }
    }
}