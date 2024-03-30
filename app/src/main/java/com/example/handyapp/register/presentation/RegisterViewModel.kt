package com.example.handyapp.register.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.handyapp.data.Handyman
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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validatePassword: ValidatePassword = ValidatePassword(),
    private val validateConfirmPassword: ValidateConfirmPassword = ValidateConfirmPassword(),
    private val validateTerms: ValidateTerms = ValidateTerms(),
    private val validatePhone: ValidatePhoneNumber = ValidatePhoneNumber(),
    val navController: NavController
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
                // User registered successfully
                val user: FirebaseUser? = auth.currentUser
                val uid = user?.uid // Retrieve the UID
                uid?.let {
                    db.collection("handymen")
                        .document(uid)
                        .set(Handyman(phoneNumber = state.phoneNumber))
                        .addOnSuccessListener {
                            Log.d("Register", "document was added")
                        }
                        .addOnFailureListener { e ->
                            println("Error adding document: $e")
                        }
                }
            } else {
                // Handle registration errors
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

}