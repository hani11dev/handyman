package com.example.handyapp.home.ProfileSettings

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.handyapp.Response
import com.example.handyapp.domain.model.HandyMan
import com.example.handyapp.domain.model.ProfileHandyMenInfo
import com.example.handyapp.domain.usecases.calcRating
import com.example.handyapp.domain.usecases.getHandyMenCardInfo
import com.example.handyapp.domain.usecases.getProfileHandyMenInfoUseCase
import com.example.handyapp.domain.usecases.updateProfessionalInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileSettingsViewModel @Inject constructor(
    private val getHandyMenCardInfo: getHandyMenCardInfo,
    private val getProfileHandyMenInfo: getProfileHandyMenInfoUseCase,
    private val calcRating: calcRating,
    private val updateProfessionalInfoUseCase: updateProfessionalInfoUseCase
) : ViewModel() {
    private var _CardInfo = mutableStateOf<Response<HandyMan>>(Response.onLoading)
    var cardInfo: State<Response<HandyMan>> = _CardInfo

    private var _profileInfo: MutableState<Response<ProfileHandyMenInfo>> =
        mutableStateOf(Response.onLoading)
    var profileInfo: State<Response<ProfileHandyMenInfo>> = _profileInfo

    private var _rating: MutableState<Response<HashMap<String, Any>>> =
        mutableStateOf(Response.onLoading)
    var rating: State<Response<HashMap<String, Any>>> = _rating

    private var _updateProfessionalInfoStatus = mutableStateOf<Response<Unit>>(Response.onLoading)
    var updateProfessionalInfoStatus : State<Response<Unit>> = _updateProfessionalInfoStatus
    init {
        viewModelScope.launch {
            getHandyMenCardInfo().collect {
                _CardInfo.value = it
            }

        }
        viewModelScope.launch {
            getProfileInfo()
        }
        viewModelScope.launch {
            calcRating().collect {
                _rating.value = it
            }
        }
    }

    fun getProfileInfo() {
        viewModelScope.launch {
            getProfileHandyMenInfo().collect {
                _profileInfo.value = it
            }
        }
    }

    fun updateProfessionalInfo(map : HashMap<String, String>){
        viewModelScope.launch {
            updateProfessionalInfoUseCase(map).collect{
                _updateProfessionalInfoStatus.value = it
            }
        }
    }
}