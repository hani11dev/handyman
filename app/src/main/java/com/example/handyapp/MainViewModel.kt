package com.example.handyapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.handyapp.navigation.Graph
import com.example.handyapp.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    var startDestination by mutableStateOf(Graph.Auth.route)
        private set
    private val _isReady = MutableStateFlow(true)
    val isReady = _isReady.asStateFlow()

    init {
        viewModelScope.launch {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser !=null){
                startDestination = Graph.Browse.route
            }
            delay(500L)
            _isReady.value = false
        }
    }
}