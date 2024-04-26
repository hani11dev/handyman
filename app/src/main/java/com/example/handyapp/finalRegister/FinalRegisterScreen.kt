package com.example.handyapp.finalRegister

import android.util.Log
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.handyapp.navigation.Graph
import com.example.handyapp.navigation.Screen
import com.example.handyapp.register.domain.components.RegistrationEvent
import com.example.handyapp.register.presentation.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun FinalRegisterScreen(navController: NavController){
    val viewModel = viewModel<FinalRegisterViewModel>()
    val state = viewModel.state
    val context = LocalContext.current
    LaunchedEffect(key1 = context){
        viewModel.validationEvents.collect{event ->
            when(event){
                is FinalRegisterViewModel.ValidationEvent.Success ->{
                    val db = FirebaseFirestore.getInstance()
// Get the current user ID (assuming the user is authenticated)
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    val userId = currentUser?.uid
                    if (userId != null) {
                        // Specify the document reference using the user ID
                        db.collection("handymen")
                            .document(userId)
                            .update(
                                hashMapOf<String, Any>(
                                    "Status" to "ACTIVE"
                                )
                            )
                            .addOnSuccessListener {

                            }
                            .addOnFailureListener { e ->
                                println("Error adding document: $e")
                            }
                    } else {
                        Log.d("Login", "login failed")
                    }
                    navController.navigate(Screen.FinishedSetupScreen.route)
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .border(width = 1.dp, color = Color.White) // Add border here
            .padding(horizontal = 16.dp), // Add padding for the content
        verticalArrangement = Arrangement.Center
    ) {
        TextField(value = state.about, onValueChange = {
            viewModel.onEvent(FinalRegistrationEvent.AboutChanged(it))},
            isError = state.aboutError != null,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "About your self") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Create, contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = state.averageSalary, onValueChange = {
            viewModel.onEvent(FinalRegistrationEvent.AverageSalaryChanged(it))},
            isError = state.averageSalaryError != null,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Average salary") },

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal
            )
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(value = state.wilaya, onValueChange = {
                viewModel.onEvent(FinalRegistrationEvent.WilayaChanged(it))},
                isError = state.wilayaError != null,
                modifier = Modifier.fillMaxWidth().weight(1f),
                placeholder = { Text(text = "Wilaya") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.LocationOn, contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text
                )
            )

            TextField(value = state.city, onValueChange = {
                viewModel.onEvent(FinalRegistrationEvent.CityChanged(it))},
                isError = state.cityError != null,
                modifier = Modifier.fillMaxWidth().weight(1f),
                placeholder = { Text(text = "City") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.LocationOn, contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text
                )
            )
        }
        TextField(value = state.street, onValueChange = {
            viewModel.onEvent(FinalRegistrationEvent.StreetChanged(it))},
            isError = state.streetError != null,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Street") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.LocationOn, contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = state.workingAreas, onValueChange = {
            viewModel.onEvent(FinalRegistrationEvent.WorkingAreaChanged(it))},
            isError = state.workingAreasError != null,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Working Areas") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.LocationOn, contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {viewModel.onEvent(FinalRegistrationEvent.Submit)}) {
            Text(text = "Submit")
        }
    }
}
















