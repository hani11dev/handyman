package com.example.handyapp.registerInfo.presentation

import ValidateCity
import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.handyapp.data.Handyman
import com.example.handyapp.register.domain.components.RegisterInfoEvent
import com.example.handyapp.registerInfo.data.userInfo
import com.example.handyapp.registerInfo.domain.use_cases.ValidateDay
import com.example.handyapp.registerInfo.domain.use_cases.ValidateFirstName
import com.example.handyapp.registerInfo.domain.use_cases.ValidateLastName
import com.example.handyapp.registerInfo.domain.use_cases.ValidateMonth
import com.example.handyapp.registerInfo.domain.use_cases.ValidateStreet
import com.example.handyapp.registerInfo.domain.use_cases.ValidateWilaya
import com.example.handyapp.registerInfo.domain.use_cases.ValidateYear
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class RegisterInfoViewModel (
    val validateFirstName: ValidateFirstName = ValidateFirstName(),
    val validateLastName: ValidateLastName = ValidateLastName(),
    val validateWilaya: ValidateWilaya = ValidateWilaya(),
    val validateCity: ValidateCity = ValidateCity(),
    val validateStreet: ValidateStreet = ValidateStreet(),
    val validateDay: ValidateDay = ValidateDay(),
    val validateMonth: ValidateMonth = ValidateMonth(),
    val validateYear: ValidateYear = ValidateYear()
): ViewModel(){

    private val validateEventChannel = Channel<RegisterInfoViewModel.ValidationEvent>()
    val validationEvents = validateEventChannel.receiveAsFlow()

    var state by mutableStateOf(RegisterInfoState())
    fun onEvent(event: RegisterInfoEvent){
        when(event){
            is RegisterInfoEvent.FirstNameChanged->{
                state = state.copy(firstName = event.firstName)
            }
            is RegisterInfoEvent.LastNameChanged->{

                state = state.copy(lastName = event.lastName)
            }

            is RegisterInfoEvent.WilayaChanged->{

                state = state.copy(wilaya = event.wilaya)
            }
            is RegisterInfoEvent.CityChanged->{

                state = state.copy(city = event.city)
            }
            is RegisterInfoEvent.StreetChanged->{

                state = state.copy(street = event.street)
            }
            is RegisterInfoEvent.DayChanged->{

                state = state.copy(day = event.day)
            }
            is RegisterInfoEvent.MonthChanged->{

                state = state.copy(month = event.month)
            }
            is RegisterInfoEvent.YearChanged->{

                state = state.copy(year = event.year)
            }
            is RegisterInfoEvent.Submit->{
                submitData()
            }
            else ->{}
        }
    }

    private fun submitData(){
        val firstNameResult = validateFirstName.excute(state.firstName)
        val lastNameResult = validateLastName.excute(state.lastName)
        val wilayaResult = validateWilaya.excute(state.wilaya)
        val cityResult = validateCity.excute(state.city)
        val streetResult = validateStreet.excute((state.street))
        val dayResult = validateDay.excute((state.day.toInt()))
        val monthResult = validateMonth.excute(state.month.toInt())
        val yearResult = validateYear.excute((state.year.toInt()))
        val hasError = listOf(
            firstNameResult,
            lastNameResult,
            wilayaResult,
            cityResult,
            streetResult,
            dayResult,
            monthResult,
            yearResult
        ).any{!it.successful}
        state = state.copy(
            firstNameError = firstNameResult.errorMessage,
            lastNameError = lastNameResult.errorMessage,
            wilayaError = wilayaResult.errorMessage,
            cityError = cityResult.errorMessage,
            streetError = streetResult.errorMessage,
            dayError = dayResult.errorMessage,
            monthError = monthResult.errorMessage,
            yearError = yearResult.errorMessage,
        )

        if(hasError) { return }
        viewModelScope.launch {
            validateEventChannel.send(RegisterInfoViewModel.ValidationEvent.Success)
        }
        val userInfo = userInfo(firstName = state.firstName, lastName = state.lastName, wilaya = state.wilaya, city = state.city, street = state.street, day = state.day, month = state.month, year = state.year)
        val userInfoMap = userInfo.toMap()
        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser

        val usersCollection = db.collection("users")
        if (user != null) {
            // User is signed in
            val userId = user.uid
            userId.let {
                db.collection("handymen")
                    .document(userId)
                    .update(
                        hashMapOf<String, Any>(
                            "city" to state.city,
                            "wilaya" to state.wilaya,
                            "street" to state.street,
                            "firstName" to state.firstName,
                            "lastName" to state.lastName,
                            "day" to state.day,
                            "month" to state.month,
                            "year" to state.year,
                        )
                    )
                    .addOnSuccessListener {
                        Log.d("Register", "document was added")
                    }
                    .addOnFailureListener { e ->
                        println("Error adding document: $e")
                    }
            }
//            db.collection("users").document(userId)
//                .set(userInfoMap)
//                .addOnSuccessListener {
//                    println("Document added with ID: $user")
//                }
//                .addOnFailureListener { e ->
//                    println("Error adding document: $e")
//                }
        } else {
            // No user is signed in
            Log.d("RegisterInfo", "not signed in")
        }



//        usersCollection.add(userInfoMap)
//            .addOnSuccessListener { documentReference ->
//                Log.d("registerInfo", "DocumentSnapshot added with ID: ${documentReference.id}")
//            }
//            .addOnFailureListener { e ->
//                Log.w("registerInfo", "Error adding document", e)
//            }
   }
    val storageRef = Firebase.storage.reference

    sealed class ValidationEvent{
        object Success: ValidationEvent()
    }
}