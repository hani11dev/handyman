package com.example.handyapp.home.Settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.handyapp.Response
import com.example.handyapp.domain.usecases.getHandyManSettingsInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor (private val logOutUseCase: logOutUseCase , private val getHandyManSettingsInfoUseCase: getHandyManSettingsInfoUseCase) : ViewModel() {

    private val _signOutState = mutableStateOf<Response<Boolean>>(Response.onSuccess(false))
    val signOutState : State<Response<Boolean>> = _signOutState

    private var _handyManSettingsInfo = mutableStateOf<Response<HashMap<String , String>>>(Response.onLoading)
    var handyManSettingsInfo : State<Response<HashMap<String , String>>> = _handyManSettingsInfo

    init {
        viewModelScope.launch {
            getHandyManSettingsInfoUseCase().collect{
                _handyManSettingsInfo.value = it
            }
        }
    }
    fun signOut(){
        viewModelScope.launch {
            logOutUseCase().collect{
                _signOutState.value = it
            }
        }
    }
}