package com.example.handyapp.finalRegister

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.handyapp.Response
import com.koDea.fixMasterClient.data.remote.dto.geoCodeReverse.GeocodeReverseResponse
import com.koDea.fixMasterClient.domain.useCases.locationUseCases.GetLocationUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FRegViewModel @Inject constructor(
    private val getLocationUseCases: GetLocationUseCases
) : ViewModel()  {
    private var _location : MutableState<Response<GeocodeReverseResponse>> = mutableStateOf(Response.onLoading)
    var location : State<Response<GeocodeReverseResponse>> = _location

    fun getLocation(lat : String , lon : String){
        viewModelScope.launch {
            getLocationUseCases(lat, lon).collect{
                _location.value = it
            }
        }
    }
}