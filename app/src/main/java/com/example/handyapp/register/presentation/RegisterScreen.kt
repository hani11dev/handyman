package com.example.handyapp.register.presentation

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
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
    val viewModel = viewModel<RegisterViewModel>(
        factory = object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return RegisterViewModel(
                    navController = navController
                )as T
            }
        }
    )
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
    Column (modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center){
        TextField(value = state.phoneNumber, onValueChange = {
            viewModel.onEvent(RegistrationEvent.PhoneNumberChanged(it))},
            isError = state.phoneNumberError != null,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Phone Number") },
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        if(state.confirmPasswordError != null){
            Text(text = state.confirmPasswordError, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row (modifier = Modifier.fillMaxWidth()){
                Checkbox(checked = state.confirmed, onCheckedChange = {viewModel.onEvent(
                    RegistrationEvent.TermsChanged(it))})

            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Accept terms")
        }

        if(state.confirmedError != null){
            Text(text = state.confirmPassword, color = MaterialTheme.colorScheme.error)
        }
        Button(onClick = {viewModel.onEvent(RegistrationEvent.Submit)},
            modifier = Modifier.align(Alignment.End)) {
                Text(text = "Submit")
        }
        Text(
            text = "Back to Login",
            modifier = Modifier.clickable {
                navController.navigate(Screen.Login.route){
                    popUpTo(Screen.Login.route){
                        inclusive = true
                    }
                }
            })
    }
}
