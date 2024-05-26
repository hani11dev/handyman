package com.example.handyapp.home.SavedJobs

import JobItem
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.handyapp.R
import com.example.handyapp.Response

@Composable
fun SavedJobsScreen(browseNavController : NavHostController,viewModel: SavedJobsViewModel = hiltViewModel()){
    when(val resp = viewModel.savedJobs.value){
        is Response.onLoading -> {
            Box(modifier = Modifier.fillMaxSize() , contentAlignment = Alignment.Center){
                CircularProgressIndicator()
            }
        }
        is Response.onFaillure -> {
            Column(modifier = Modifier.fillMaxSize() , verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally){
                Image(painter = painterResource(R.drawable.error_illust), contentDescription = resp.message)

            }

        }
        is Response.onSuccess -> {
            if (resp.data.isNotEmpty()){
                LazyColumn (Modifier.fillMaxSize()){
                    items(resp.data){
                        JobItem(job = it, navHostController = browseNavController)
                    }
                }

            }else{
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.no_data),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    )
                    Text(
                        text = "You didn't save any Job",
                        Modifier.padding(bottom = 24.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

        }
    }
}