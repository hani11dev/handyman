package com.example.handyapp.home.myRequests

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.navigation.NavController


data class Request(
    val name: String,
    val service: String,
    val price: Int,
    val currency: String,
    val date: String,
    val location : String
)



@Composable
fun RequestItem(request: Request) {
    val backgroundColor = MaterialTheme.colorScheme.surface

    Surface(
        color = backgroundColor,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        shadowElevation = 10.dp,
        tonalElevation = 5.dp,
        border = BorderStroke(2.dp, color = Color.Gray)
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = backgroundColor)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = request.service,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .align(Alignment.CenterHorizontally), // Aligning text horizontally
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = 18.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Client: \n${request.name}",
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Text(
                        text = "From price: \n${request.price}${request.currency}",
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Location: \n${request.location}",
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Text(
                        text = "Until: \n${request.date}",
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { /* Handle accept */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6CC470)
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(text = "Accept")
                    }


                    Button(
                        onClick = { /* Handle decline */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEE7373)
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp)

                    ) {
                        Text(text = "Decline")
                    }


                    ClickableText(
                        text = AnnotatedString.Builder().apply {
                            withStyle(style = SpanStyle(textDecoration =
                            TextDecoration.Underline ,color = Color.Gray)
                            ) {
                                append("Detail")
                            }

                        }.toAnnotatedString(),
                        onClick = {
                            // Handle details
                        },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                }
            }
        }
    }
}





val requests = listOf(
    Request("Mohamed", "Apartment Cleaning",
        3000, "DZD", "02.02.2024", "Zeralda"),
    Request("Sarah", "Electrical help",
        12000, "DZD", "16.05.2024","nouvelle ville"),
    Request("Ahmed", "Home repairs",
        5000, "DZD", "18.05.2024","blida"),
    Request("issam", "Painting",
        9200, "DZD", "12.05.2024","oran"),

    )

@Composable
fun MyRequestsScreen(navController: NavController) {
    LazyColumn(
        //   modifier = Modifier.weight(1f)
    ) {
        items(requests.size) {
                request->
            RequestItem(requests[request])}
    }
}
