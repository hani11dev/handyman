package com.example.handyapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.handyapp.navigation.Graph
import com.example.handyapp.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    var startDestination by mutableStateOf(Graph.Auth.route)
        private set
    var subStartDestination by mutableStateOf(Screen.Login.route)
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



    /*init {
        viewModelScope.launch {
            val currentUser = FirebaseAuth.getInstance().currentUser
            try {
                if (currentUser !=null){
                    startDestination = Graph.Browse.route
                    val status = FirebaseFirestore.getInstance().collection("HandyMan").document(currentUser.uid).get().await().get("Status")
                    if(status != null){
                        if(status == "NEW"){
                            //navController.navigate(Screen.RegisterInfo.route)
                            startDestination = Graph.State.route
                            subStartDestination = Screen.RegisterInfo.route
                        }else if(status == "WAITING"){
                            //navController.navigate(Graph.Waiting.route)
                            startDestination = Graph.State.route
                            subStartDestination = Screen.Waiting.route
                        }else if(status == "ACCEPTED"){
                            //navController.navigate(Graph.Accepted.route)
                            startDestination = Graph.State.route
                            subStartDestination = Screen.OneLastStepScreen.route
                        }else if(status == "REFUSED"){
                            //navController.navigate(Graph.Refused.route)
                            startDestination = Graph.State.route
                            subStartDestination = Screen.Refused.route
                        }else if(status == "ACTIVE"){
                            //navController.navigate(Graph.Browse.route)
                            startDestination = Graph.Jobs.route
                            subStartDestination = Screen.Waiting.route
                        }
                    }
                }

            }catch (e:Exception){}
            delay(500L)
            _isReady.value = false
        }
    }*/
}