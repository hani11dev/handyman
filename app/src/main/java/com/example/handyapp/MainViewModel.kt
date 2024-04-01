package com.example.handyapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.handyapp.navigation.Graph
import com.example.handyapp.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    var startDestination by mutableStateOf(Graph.Auth.route)
        private set

    init {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser !=null){
            startDestination = Graph.Browse.route
        }
    }
}