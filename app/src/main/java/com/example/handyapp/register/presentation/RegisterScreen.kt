package com.example.handyapp.register.presentation

import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.handyapp.navigation.Screen
import com.example.handyapp.register.domain.components.RegistrationEvent
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

@Composable
fun RegisterScreen1(
    navController: NavController
){
    val viewModel = viewModel<RegisterViewModel>()
    val state = viewModel.state
    val context = LocalContext.current
    var phoneNumberVerified by rememberSaveable {
        mutableStateOf(false)
    }
    val auth = FirebaseAuth.getInstance()

    var verificationCode by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(key1 = context){
        viewModel.validationEvents.collect{event ->
            when(event){
                is RegisterViewModel.ValidationEvent.Success ->{
                    navController.navigate(Screen.RegistrationSucceeded.route)
                }
            }
        }
    }
    var phoneNumberReadOnly by rememberSaveable {
        mutableStateOf(false)
    }
    Column (    modifier = Modifier
        .fillMaxSize()
        .border(width = 1.dp, color = Color.White) // Add border here
        .padding(horizontal = 16.dp), // Add padding for the content
        verticalArrangement = Arrangement.Center
    ){
        TextField(value = state.phoneNumber, onValueChange = {
            viewModel.onEvent(RegistrationEvent.PhoneNumberChanged(it))},
            isError = state.phoneNumberError != null,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Phone Number") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Phone, contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            readOnly = phoneNumberReadOnly
        )
        if(state.phoneNumberError != null){
            Text(text = state.phoneNumberError, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(16.dp))

        TextField(value = state.email, onValueChange = {
            viewModel.onEvent(RegistrationEvent.EmailChanged(it))},
            isError = state.emailError != null,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Email") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Email, contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        if(state.emailError != null){
            Text(text = state.emailError, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(16.dp))

        TextField(value = state.password, onValueChange = {
            viewModel.onEvent(RegistrationEvent.PasswordChanged(it))},
            isError = state.passwordError != null,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Password") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Lock, contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation() // Hides the password text
        )
        if(state.passwordError != null){
            Text(text = state.passwordError, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(16.dp))

        TextField(value = state.confirmPassword, onValueChange = {
            viewModel.onEvent(RegistrationEvent.ConfirmPasswordChanged(it))},
            isError = state.confirmPasswordError != null,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Confirm Password") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Lock, contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation() // Hides the password text
        )
        if(state.confirmPasswordError != null){
            Text(text = state.confirmPasswordError, color = MaterialTheme.colorScheme.error)

        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = state.confirmed,
                onCheckedChange = { viewModel.onEvent(RegistrationEvent.TermsChanged(it)) }
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(text = "Accept terms")
        }
        var showOTPField by rememberSaveable {
            mutableStateOf(false)
        }


        if(state.confirmedError != null){
            Text(text = state.confirmPassword, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(16.dp))
        var showVerifyButton by rememberSaveable {
            mutableStateOf(true)
        }
        /*AnimatedVisibility(visible = showVerifyButton) {
            Button(onClick = {
                if (state.phoneNumber.isNotEmpty()){
                    var phone = state.phoneNumber.drop(1)
                    phone = "+213" + phone
                    Toast.makeText(context , "phone :"+ phone , Toast.LENGTH_SHORT).show()

                    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                            statusMessage = "Verification completed automatically"
                        }

                        override fun onVerificationFailed(e: FirebaseException) {
                            statusMessage = "Verification failed: ${e.message}"
                            Toast.makeText(context , "failled:" + statusMessage , Toast.LENGTH_SHORT).show()

                        }

                        override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                            super.onCodeSent(id, token)
                            verificationId = id // Store verification ID
                            statusMessage = "SMS code sent"
                            showOTPField = true
                        }
                    }

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phone,
                        60,
                        TimeUnit.SECONDS,
                        context as Activity,
                        callbacks
                    )

                }
            }) {
                Text("verify phone number")
            }

        }

        AnimatedVisibility(visible = showOTPField) {
            Column {


                *//*TextField(
                    value = verificationCode,
                    onValueChange = { verificationCode = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp))*//*
                Spacer(modifier = Modifier.height(4.dp))
                Box(modifier = Modifier.fillMaxWidth() , contentAlignment = Alignment.Center){
                    OtpTextField(otpText = verificationCode , onOtpTextChange = {value ,otpInputFilled ->
                        verificationCode = value
                    })

                }
                Spacer(modifier = Modifier.height(4.dp))


                Button(onClick = {
                    verificationId?.let {
                        //verifyPhoneNumberWithCode(it, verificationCode)
                        val credential = PhoneAuthProvider.getCredential(it, verificationCode)
                        auth.signInWithCredential(credential).addOnSuccessListener {
                            statusMessage = "Phone verified"
                            Toast.makeText(context, statusMessage, Toast.LENGTH_SHORT).show()
                            auth.signOut()
                            showOTPField = false
                            showVerifyButton = false
                            phoneNumberReadOnly = true
                            phoneNumberVerified = true
                        }.addOnFailureListener {
                            statusMessage = "phone verification failled"
                            Toast.makeText(context, statusMessage, Toast.LENGTH_SHORT).show()

                        }

                    }
                }) {
                    Text(text = "verifyCode")
                }
            }
        }*/
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    //if (phoneNumberVerified)
                        viewModel.onEvent(RegistrationEvent.Submit)
               // else Toast.makeText(context , "please verify your number" , Toast.LENGTH_SHORT).show()
                }
            ) {
                Text(text = "Submit")
            }

            Spacer(modifier = Modifier.weight(1f)) // Flexible space to fill the gap

            Text(
                text = "Back to Login",
                modifier = Modifier.clickable {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }




    }
}

