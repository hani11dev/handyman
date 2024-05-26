package com.example.handyapp.login.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.handyapp.R
import com.example.handyapp.Response
import com.example.handyapp.navigation.Graph
import com.example.handyapp.navigation.Screen
import com.example.handyapp.register.domain.components.LoginEvent
import com.example.handyapp.register.domain.components.RegistrationEvent
import com.example.handyapp.register.presentation.RegisterViewModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel(),
    context : Context = LocalContext.current
){
    /*val viewModel = viewModel<LoginViewModel>(
        factory = object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(
                    navController = navController
                ) as T
            }
        }
    )*/
    //val state = viewModel.state
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
    Column (
        modifier = Modifier
            .fillMaxSize()
            .border(width = 1.dp, color = Color.White) // Add border here
            .padding(horizontal = 16.dp), // Add padding for the content
        verticalArrangement = Arrangement.Center

       ){
        
        Row (modifier = Modifier
            .fillMaxWidth()
            .height(192.dp) , verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.Center){
            Image(painter = painterResource(R.drawable.welcome_illus), contentDescription = null , modifier = Modifier
                .size(192.dp)
                .weight(1f))
            Column (modifier = Modifier.weight(1f) , verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally){
                Text(text = "Welcome" ,fontWeight = FontWeight.SemiBold , fontSize = 22.sp)
                Text("LogIn for browsing")
            }
        }
        TextField(value = email, onValueChange = {
            //viewModel.onEvent(LoginEvent.EmailChanged(it))
                                                 email = it
            emailError = false
            emailSupportingText = ""
                                                 },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Email") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Email, contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            supportingText = {Text(text = emailSupportingText,color = MaterialTheme.colorScheme.error)},
            isError = emailError
        )
        Spacer(modifier = Modifier.height(16.dp))
        var passwordVisibility by remember {
            mutableStateOf(false)
        }

        TextField(value = password, onValueChange = {
            //viewModel.onEvent(LoginEvent.PasswordChanged(it))
                                                    password = it
            passwordError = false
            passwordSupportingText = ""
                                                    },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Password") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Lock, contentDescription = null
                )
            }, trailingIcon = {
                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    Icon(
                        imageVector = if (!passwordVisibility) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        contentDescription = null
                    )
                }
            },
            isError = passwordError,
            supportingText = {Text(text = passwordSupportingText
                ,color = MaterialTheme.colorScheme.error)},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (!passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None // Hides the password text
        )

        Spacer(modifier = Modifier.height(8.dp))
        var progressBarState = remember {
            mutableStateOf(false)
        }
        Button( shape = RoundedCornerShape(8.dp),onClick = {
            //viewModel.onEvent(LoginEvent.Submit)

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
                    passwordSupportingText = "Password length should be more than 8 caractere"
                } else {
                    passwordError = false
                }
            }
            if (!emailError && !passwordError){
                progressBarState.value =true
                viewModel.signIn(email , password)
            }
                         },
            modifier = Modifier.fillMaxWidth()) {
            when(val resp = viewModel.signInState.value){
                is Response.onLoading -> {}
                is Response.onFaillure -> {
                    LaunchedEffect(true) {
                        progressBarState.value = false
                        Toast.makeText(context , resp.message , Toast.LENGTH_SHORT).show()

                    }
                }
                is Response.onSuccess -> {
                    progressBarState.value = false
                    navController.navigate(Graph.Browse.route){
                            popUpTo(Graph.Auth.route){
                                inclusive = true
                            }

                    }
                }
            }
            Text(text = "Login")
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
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text(text = "Not registered yet? ")

            Text(text = "Register now ", modifier = Modifier.clickable {
                navController.navigate(route = Screen.Register.route)
            }, color = MaterialTheme.colorScheme.primary , fontWeight = FontWeight.Medium)
        }
    }

}