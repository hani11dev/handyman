package com.example.handyapp.login.presentation

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.handyapp.navigation.Screen
import com.example.handyapp.register.domain.components.LoginEvent
import com.example.handyapp.register.domain.components.RegistrationEvent
import com.example.handyapp.register.presentation.RegisterViewModel

@Composable
fun LoginScreen(
    navController: NavController
){
    val viewModel = viewModel<LoginViewModel>(
        factory = object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(
                    navController = navController
                ) as T
            }
        }
    )
    val state = viewModel.state
    Column (
        modifier = Modifier
            .fillMaxSize()
            .border(width = 1.dp, color = Color.White) // Add border here
            .padding(horizontal = 16.dp), // Add padding for the content
        verticalArrangement = Arrangement.Center

       ){
        TextField(value = state.email, onValueChange = {
            viewModel.onEvent(LoginEvent.EmailChanged(it))},
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Email") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Email, contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = state.password, onValueChange = {
            viewModel.onEvent(LoginEvent.PasswordChanged(it))},
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

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {viewModel.onEvent(LoginEvent.Submit)},
            modifier = Modifier.align(Alignment.End)) {
            Text(text = "Login")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text(text = "Not registered yet? ")

            Text(text = "Register now ", modifier = Modifier.clickable {
                navController.navigate(route = Screen.Register.route)
            })
        }
    }

}