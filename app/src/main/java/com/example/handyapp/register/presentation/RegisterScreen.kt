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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.handyapp.Response
import com.example.handyapp.navigation.Screen
import com.example.handyapp.register.domain.components.RegistrationEvent
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

@Composable
fun RegisterScreen1(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    /*val viewModel = viewModel<RegisterViewModel>()
    val state = viewModel.state*/
    val context = LocalContext.current
    var phoneNumberVerified by rememberSaveable {
        mutableStateOf(false)
    }
    val auth = FirebaseAuth.getInstance()

    var verificationCode by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf<String?>(null) }
   /* LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is RegisterViewModel.ValidationEvent.Success -> {
                    navController.navigate(Screen.RegistrationSucceeded.route)
                }
            }
        }
    }*/
    var phoneNumberReadOnly by rememberSaveable {
        mutableStateOf(false)
    }

    var email by rememberSaveable {
        mutableStateOf("")
    }
    var emailError by rememberSaveable {
        mutableStateOf(false)
    }
    var emailSupportingText by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    var passwordError by rememberSaveable {
        mutableStateOf(false)
    }
    var passwordSupportingText by rememberSaveable {
        mutableStateOf("")
    }
    var phoneNumber by rememberSaveable {
        mutableStateOf("")
    }
    var phoneNumberError by rememberSaveable {
        mutableStateOf(false)
    }
    var phoneNumberSupportingText by rememberSaveable {
        mutableStateOf("")
    }
    var confirmPassword by rememberSaveable {
        mutableStateOf("")
    }

    var confirmPasswordError by rememberSaveable {
        mutableStateOf(false)
    }
    var confirmPasswordSupportingText by rememberSaveable {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .border(width = 1.dp, color = Color.White) // Add border here
            .padding(horizontal = 16.dp), // Add padding for the content
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = phoneNumber, onValueChange = {
                //viewModel.onEvent(RegistrationEvent.PhoneNumberChanged(it))
                                                 phoneNumber = it
                phoneNumberError = false
                phoneNumberSupportingText = ""
            },
            isError = phoneNumberError,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Phone Number") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Phone, contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            readOnly = phoneNumberReadOnly,
            supportingText ={Text(text = phoneNumberSupportingText,color = MaterialTheme.colorScheme.error)}
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email, onValueChange = {
               // viewModel.onEvent(RegistrationEvent.EmailChanged(it))
                                           email = it
                emailError = false
                emailSupportingText = ""
            },
            isError = emailError,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Email") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Email, contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            supportingText = {Text(text = emailSupportingText,color = MaterialTheme.colorScheme.error)}
        )

        Spacer(modifier = Modifier.height(16.dp))
        var passwordVisibility by remember {
            mutableStateOf(false)
        }

        TextField(value = password, onValueChange = {
            //viewModel.onEvent(RegistrationEvent.PasswordChanged(it))
                                                    password = it
            passwordError = false
            passwordSupportingText = ""
        },
            isError = passwordError,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Password") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Lock, contentDescription = null
                )
            }, trailingIcon = {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(
                        imageVector = if (!passwordVisibility) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        contentDescription = null
                    )
                }
            },
            supportingText = {Text(text = passwordSupportingText,color = MaterialTheme.colorScheme.error)},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (!passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None // Hides the password text
        )

        Spacer(modifier = Modifier.height(16.dp))
        var confirmPasswordVisibility by remember {
            mutableStateOf(false)
        }

        TextField(
            value = confirmPassword, onValueChange = {
                //viewModel.onEvent(RegistrationEvent.ConfirmPasswordChanged(it))
                                                     confirmPassword = it
                confirmPasswordError = false
                confirmPasswordSupportingText = ""
            },
            isError = confirmPasswordError,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Confirm Password") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Lock, contentDescription = null
                )
            },
            trailingIcon = {
                IconButton(onClick = {
                    confirmPasswordVisibility = !confirmPasswordVisibility
                }) {
                    Icon(
                        imageVector = if (!confirmPasswordVisibility) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        contentDescription = null
                    )
                }
            },
            supportingText = {Text(text = confirmPasswordSupportingText,color = MaterialTheme.colorScheme.error)},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (!confirmPasswordVisibility) PasswordVisualTransformation() else VisualTransformation.None // Hides the password text
        )

        Spacer(modifier = Modifier.height(16.dp))
        /*Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = state.confirmed,
                onCheckedChange = { viewModel.onEvent(RegistrationEvent.TermsChanged(it)) }
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(text = "Accept terms")
        }*/
        var showOTPField by rememberSaveable {
            mutableStateOf(false)
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
            var progressBarState = remember {
                mutableStateOf(false)
            }
            Button(
                onClick = {

                    //if (phoneNumberVerified)
                    //viewModel.onEvent(RegistrationEvent.Submit)
                    // else Toast.makeText(context , "please verify your number" , Toast.LENGTH_SHORT).show()
                    if (email.isEmpty()) {
                        emailError = true
                        emailSupportingText = "Email can't be empty"

                    }
                    if (password.isEmpty()) {
                        passwordError = true
                        passwordSupportingText = "Password can't be empty"
                    } else {
                        if (password.length < 8) {
                            passwordError = true
                            passwordSupportingText =
                                "Password length should be more than 8 caractere"
                        } else {
                            passwordError = false
                        }
                    }
                    if (confirmPassword.isEmpty()){
                        confirmPasswordError = true
                        confirmPasswordSupportingText = "Confirmation password can't be empty"
                    }
                    if (!password.equals(confirmPassword)){
                        confirmPasswordError = true
                        confirmPasswordSupportingText = "password not matches"
                    }
                    if (phoneNumber.isEmpty() || phoneNumber.length < 10){
                        phoneNumberError = true
                        phoneNumberSupportingText = "Invalid phone number"
                    }
                    if (!emailError && !passwordError && !confirmPasswordError){
                        progressBarState.value = true
                        viewModel.signUp(email , password , phoneNumber)
                    }
                }
            ) {
                when(val resp = viewModel.signUpState.value){
                    is Response.onLoading -> {}
                    is Response.onFaillure -> {progressBarState.value = false
                    Toast.makeText(context , resp.message , Toast.LENGTH_SHORT).show()}
                    is Response.onSuccess -> {
                        progressBarState.value = false
                        navController.navigate(Screen.Login.route){
                            navController.popBackStack()
                        }
                    }
                }
                Text(text = "Submit")
                if (progressBarState.value) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier
                            .size(26.dp)
                            .fillMaxSize(),
                        strokeWidth = 2.5.dp
                    )
                }
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

