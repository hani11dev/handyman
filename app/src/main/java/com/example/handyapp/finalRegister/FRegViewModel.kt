package com.example.handyapp.finalRegister

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.handyapp.Response
import com.example.handyapp.domain.usecases.updateHandyManInfoUseCase
import com.koDea.fixMasterClient.data.remote.dto.geoCodeReverse.GeocodeReverseResponse
import com.koDea.fixMasterClient.domain.useCases.locationUseCases.GetLocationUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FRegViewModel @Inject constructor(
    private val getLocationUseCases: GetLocationUseCases,
    private val updateHandyManInfoUseCase: updateHandyManInfoUseCase
) : ViewModel()  {
    private var _location : MutableState<Response<GeocodeReverseResponse>> = mutableStateOf(Response.onLoading)
    var location : State<Response<GeocodeReverseResponse>> = _location

    private var _updateState : MutableState<Response<Unit>> = mutableStateOf(Response.onLoading)
    var updateState : State<Response<Unit>> = _updateState

    fun getLocation(lat : String , lon : String){
        viewModelScope.launch {
            getLocationUseCases(lat, lon).collect{
                _location.value = it
            }
        }
    }

    fun updateInfo(about : String ,services : String ,workingAreas : String , averageSalary : Double , city : String , wilaya : String , street : String , lat : String , long: String , portfolio : List<Uri>){
        viewModelScope.launch {
            updateHandyManInfoUseCase(about, services,workingAreas, averageSalary, city, wilaya, street, lat, long , portfolio).collect{
                _updateState.value = it
            }
        }
    }


}