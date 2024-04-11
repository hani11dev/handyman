package com.example.handyapp.home.jobs
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.handyapp.R
@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BiddingScreen(viewModel: JobsDetailsViewModel = hiltViewModel()) {
    val jobID: String = viewModel.jobID.value
    var descriptionSupportingText by rememberSaveable { mutableStateOf("") }
    var daySupportingText by rememberSaveable { mutableStateOf("") }
    var hourSupportingText by rememberSaveable { mutableStateOf("") }
    var estimatedPriceSupportingText by rememberSaveable { mutableStateOf("") }


    var description by rememberSaveable { mutableStateOf("") }
    var day by rememberSaveable { mutableStateOf("") }
    var estimatedPrice by rememberSaveable { mutableStateOf("") }

    var descriptionError by rememberSaveable { mutableStateOf(false) }
    var dayError by rememberSaveable { mutableStateOf(false) }
    var estimatedPriceError by rememberSaveable { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .background(
                color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.background else colorResource(
                    id = R.color.white
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Place Your Bid",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it
                    descriptionSupportingText = ""},
                label = { Text("Description") },
                isError = descriptionError,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Text(
                text = "Time :",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp)
            )
            OutlinedTextField(
                value = day,
                onValueChange = { day = it
                    daySupportingText = ""},
                label = { Text("Time") },
                isError = dayError,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Text(
                text = "Estimated Price :",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp)
            )

            OutlinedTextField(
                value = estimatedPrice,
                onValueChange = {
                    if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                        estimatedPrice = it
                        estimatedPriceSupportingText = ""
                    }
                },
                label = { Text("Price") },
                isError = estimatedPriceError,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            var progressState by rememberSaveable { mutableStateOf(false) }
            Button(


                onClick = {

                    descriptionError = false
                    dayError = false
                    estimatedPriceError = false

                    if (description.isEmpty() || day.isEmpty() || estimatedPrice.isEmpty()) {

                        progressState = false

                        if (description.isEmpty()) {
                            descriptionError = true
                            descriptionSupportingText="description can't be empty"
                        }
                        if (day.isEmpty()) {
                            dayError = true
                            daySupportingText= "Day can't be empty"
                        }
                        if (estimatedPrice.isEmpty()) {
                            estimatedPriceError = true
                            estimatedPriceSupportingText="price can't be empty"
                        }
                    } else {
                        var bid=ABid(
                            description,
                            day,
                        )
                        addBid(bid, jobID)
                    }
                }) {
                Text(text = "Submit")
            }

        }
    }
}
