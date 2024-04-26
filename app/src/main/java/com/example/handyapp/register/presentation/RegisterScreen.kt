package com.example.handyapp.register.presentation

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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

@Composable
fun RegisterScreen1(
    navController: NavController
){
    val viewModel = viewModel<RegisterViewModel>()
    val state = viewModel.state
    val context = LocalContext.current
    LaunchedEffect(key1 = context){
        viewModel.validationEvents.collect{event ->
            when(event){
                is RegisterViewModel.ValidationEvent.Success ->{
                    navController.navigate(Screen.RegistrationSucceeded.route)
                }
            }
        }
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
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
            verticalAlignment = Alignment.CenterVertically // Aligns children vertically
        ) {
            Checkbox(
                checked = state.confirmed,
                onCheckedChange = { viewModel.onEvent(RegistrationEvent.TermsChanged(it)) }
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(text = "Accept terms")
        }


        if(state.confirmedError != null){
            Text(text = state.confirmPassword, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { viewModel.onEvent(RegistrationEvent.Submit) }
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

