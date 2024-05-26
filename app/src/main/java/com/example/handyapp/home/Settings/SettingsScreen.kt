package com.example.handyapp.home.Settings

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.handyapp.R
import com.example.handyapp.Response
import com.example.handyapp.navigation.Graph

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    rootNavController: NavHostController,
    browseNavController : NavHostController,
    viewModel: SettingsViewModel = hiltViewModel(),
    context: Context = LocalContext.current
) {

        Column(
            modifier = Modifier
                .fillMaxSize()
            
        ) {

            var progressState = rememberSaveable {
                mutableStateOf(false)
            }
            val scope = rememberCoroutineScope()
            Button(
                onClick = {
                    viewModel.signOut()
                    when (val resp = viewModel.signOutState.value) {
                        is Response.onLoading -> {
                            progressState.value = true
                        }

                        is Response.onFaillure -> {
                            Toast.makeText(context, resp.message, Toast.LENGTH_SHORT).show()
                        }

                        is Response.onSuccess -> {
                            rootNavController.navigate(Graph.Auth.route){
                                popUpTo(Graph.Browse.route){
                                    inclusive = true
                                }
                            }
                        }
                    }


                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row {
                    Text(text = "Log Out")
                    if (progressState.value)
                        CircularProgressIndicator()
                }
            }
        }


}
