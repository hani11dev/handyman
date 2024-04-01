package com.example.handyapp.finalRegister

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.handyapp.register.domain.components.RegistrationEvent
import com.example.handyapp.register.presentation.RegisterState
import com.example.handyapp.register.presentation.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class FinalRegisterViewModel(
    private val validateCity: ValidateCity = ValidateCity(),
    private val validateStreet: ValidateStreet = ValidateStreet(),
    private val validateWilaya: ValidateWilaya = ValidateWilaya(),
    private val validateAverageSalary: ValidateAverageSalary = ValidateAverageSalary(),
    private val validateAbout: ValidateAbout = ValidateAbout(),
    private val validateWorkingAreas: ValidateWorkingAreas = ValidateWorkingAreas()

): ViewModel() {
    var state by mutableStateOf(FinalRegisterState())

    private val validateEventChannel = Channel<ValidationEvent>()
    val validationEvents = validateEventChannel.receiveAsFlow()
    fun onEvent(event: FinalRegistrationEvent){
        when(event){
            is FinalRegistrationEvent.AboutChanged->{
                state = state.copy(about = event.about)
            }
            is FinalRegistrationEvent.AverageSalaryChanged->{

                state = state.copy(averageSalary = event.averageSalary)
            }
            is FinalRegistrationEvent.WilayaChanged->{
                state = state.copy(wilaya = event.wilaya)
            }
            is FinalRegistrationEvent.CityChanged->{
                state = state.copy(city =  event.city)
            }
            is FinalRegistrationEvent.StreetChanged->{
                state = state.copy(street =  event.street)
            }
            is FinalRegistrationEvent.WorkingAreaChanged->{
                state = state.copy(workingAreas =  event.workingAreas)
            }
            is FinalRegistrationEvent.Submit->{
                submitData()
            }
            else -> {}
        }
    }

    private fun submitData(){
        val aboutResult = validateAbout.excute(about = state.about)
        val wilayaResult = validateWilaya.excute(wilaya = state.wilaya)
        val cityResult = validateCity.excute(city = state.city)
        val streetResult = validateStreet.excute(street = state.street)
        val averageSalaryResult = validateAverageSalary.excute(averageSalary = state.averageSalary)
        val workingAreasResult = validateWorkingAreas.excute(workingAreas = state.workingAreas)
        val hasError = listOf(
            aboutResult,
            wilayaResult,
            cityResult,
            streetResult,
            averageSalaryResult,
            workingAreasResult
        ).any{!it.successful}
        state = state.copy(
            aboutError = aboutResult.errorMessage,
            wilayaError = wilayaResult.errorMessage,
            cityError = cityResult.errorMessage,
            streetError = streetResult.errorMessage,
            averageSalaryError = averageSalaryResult.errorMessage,
            workingAreasError = workingAreasResult.errorMessage
        )
        if(hasError) { return }

        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser

        val usersCollection = db.collection("handymen")
        if (user != null) {
            // User is signed in
            val userId = user.uid
            userId.let {
                db.collection("handymen")
                    .document(userId)
                    .update(
                        hashMapOf<String, Any>(
                            "Wilaya" to state.wilaya,
                            "City" to state.city,
                            "Street" to state.street,
                            "AverageSalary" to state.averageSalary,
                            "WorkingAreas" to state.workingAreas,
                            "About" to state.about,
                            "status" to "ACTIVE"
                        )
                    )
                    .addOnSuccessListener {

                    }
                    .addOnFailureListener { e ->
                        println("Error adding document: $e")
                    }
                val handyMenCollectionRef = db.collection("handymen").document(userId)
                val reviewSubCol = handyMenCollectionRef.collection("reviews")
                val requests = handyMenCollectionRef.collection("requests")

            }
            viewModelScope.launch {
                validateEventChannel.send(FinalRegisterViewModel.ValidationEvent.Success)
            }
        } else {
            // No user is signed in
            Log.d("FinalRegister", "last else")
        }
    }
    sealed class ValidationEvent{
        object Success: ValidationEvent()
    }
}