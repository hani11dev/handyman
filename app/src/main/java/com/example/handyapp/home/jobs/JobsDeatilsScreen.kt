package com.example.handyapp.home.jobs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun JobDetailsScreen(viewModel: JobsDetailsViewModel = hiltViewModel()){
    val jobID : String = viewModel.jobID.value
    Box(modifier = Modifier.fillMaxSize() , contentAlignment = Alignment.Center){
        Text(jobID)
    }
}