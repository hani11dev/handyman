package com.example.handyapp.register.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.handyapp.Response
import com.example.handyapp.data.Handyman
import com.example.handyapp.domain.usecases.signUpUseCase
import com.example.handyapp.navigation.Screen
import com.example.handyapp.register.domain.use_case.ValidateConfirmPassword
import com.example.handyapp.register.domain.use_case.ValidateEmail
import com.example.handyapp.register.domain.use_case.ValidatePassword
import com.example.handyapp.register.domain.use_case.ValidatePhoneNumber
import com.example.handyapp.register.domain.use_case.ValidateTerms
import com.example.handyapp.register.domain.components.RegistrationEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.onesignal.OneSignal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val signUpUseCase: signUpUseCase) : ViewModel(){
    private var _signUpState = mutableStateOf<Response<Unit>>(Response.onLoading)
    var signUpState : State<Response<Unit>> = _signUpState
    fun signUp(email : String , password : String , phoneNumber : String){
        viewModelScope.launch {
            signUpUseCase(email, password, phoneNumber) .collect{
                _signUpState.value = it
            }
        }
    }
}
/*
class RegisterViewModel(
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validatePassword: ValidatePassword = ValidatePassword(),
    private val validateConfirmPassword: ValidateConfirmPassword = ValidateConfirmPassword(),
    private val validateTerms: ValidateTerms = ValidateTerms(),
    private val validatePhone: ValidatePhoneNumber = ValidatePhoneNumber(),
    ): ViewModel(){
    var state by mutableStateOf(RegisterState())

    private val validateEventChannel = Channel<ValidationEvent>()
    val validationEvents = validateEventChannel.receiveAsFlow()

    private fun createFireBaseUser(email: String, password: String){
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
                task ->
            if (task.isSuccessful) {
                val user: FirebaseUser? = auth.currentUser
                val uid = user?.uid
                val Handyman = hashMapOf(
                    "PhoneNumber" to state.phoneNumber,
                    "Status" to "NEW",
                    "Email" to state.email,
                    "DeviceToken" to OneSignal.User.pushSubscription.id,
                    "FirstName" to "",
                    "LastName" to "",
                    "Day" to "",
                    "Month" to "",
                    "Year" to "",
                    "Rating" to 0.toDouble(),
                    "nbReviews" to 0.toDouble(),
                    "OrdersCompleted" to "0",
                    "Category" to "",
                    "About" to "",
                    "WorkingAreas" to "",
                    "SubCategory" to "",
                    "ProfileImage" to "",
                    "Latitude" to "",
                    "Longitude" to "",
                )
                uid?.let {
                    db.collection("HandyMan")
                        .document(uid)
                        .set(Handyman)
                        .addOnSuccessListener {
                            Log.d("Register", "document was added")
                        }
                        .addOnFailureListener { e ->
                            println("Error adding document: $e")
                        }
                }
            } else {
                println("Registration error: ${task.exception?.message}")
            }
        }.addOnFailureListener{
            Log.d("Register", "Failure")
        }
    }

    fun onEvent(event: RegistrationEvent){
        when(event){
            is RegistrationEvent.EmailChanged->{
                state = state.copy(email = event.email)
            }
            is RegistrationEvent.PasswordChanged->{

                state = state.copy(password = event.password)
            }
            is RegistrationEvent.ConfirmPasswordChanged->{
                state = state.copy(confirmPassword = event.repeatedPassword)
            }
            is RegistrationEvent.TermsChanged->{
                state = state.copy(confirmed =  event.accepted)
            }
            is RegistrationEvent.Submit->{
                submitData()
            }
            is RegistrationEvent.PhoneNumberChanged->{
                state = state.copy(phoneNumber = event.phoneNumber)
            }
        }
    }

    private fun submitData() {
        val emailResult = validateEmail.execute(state.email)
        val passwordResult = validatePassword.execute(state.password)
        val confirmPasswordResult = validateConfirmPassword.execute(state.password, state.confirmPassword)
        val termsResult = validateTerms.execute(state.confirmed)
        val phoneNumberResult = validatePhone.execute(state.phoneNumber)
        val hasError = listOf(
            emailResult,
            passwordResult,
            confirmPasswordResult,
            termsResult,
            phoneNumberResult
        ).any{!it.successful}
            state = state.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                confirmPasswordError = confirmPasswordResult.errorMessage,
                confirmedError = termsResult.errorMessage,
                phoneNumberError = phoneNumberResult.errorMessage
            )
        if(hasError) { return }
        createFireBaseUser(email = state.email, password = state.password)
        viewModelScope.launch {
            validateEventChannel.send(ValidationEvent.Success)
        }
        
    }
    sealed class ValidationEvent{
        object Success: ValidationEvent()
    }

}*/
