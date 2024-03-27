package com.example.handyapp.home.myRequests


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.handyapp.R
data class Request(
    val name: String,
    val service: String,
    val price: Int,
    val currency: String,
    val date: String,
    var profilePicture: Int? = null // Int? to hold the resource ID of the profile picture
)

@Composable
fun RequestItem(request: Request) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        //color = Color.White, // Set background color to white
        shadowElevation = 10.dp, // Add elevation for shadow effect
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
            // colors = CardDefaults.cardColors(containerColor = Color.White)
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE))
            // elevation = CardDefaults.elevation()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    request.profilePicture?.let { profilePicture ->
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .clickable { /* Handle image click */ }
                        ) {
                            Image(
                                painter = painterResource(id = profilePicture),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .weight(1f)
                    ) {
                        Text(
                            text = request.name,
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.Black,
                            fontSize = 18.sp
                        )
                        Text(
                            text = request.service,
                            color = Color.Black,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "From price: \n${request.price}${request.currency}",
                        modifier = Modifier.padding(top = 8.dp),
                        color = Color.Black
                    )

                    Text(
                        text = "Until: \n${request.date}",
                        modifier = Modifier.padding(top = 8.dp),
                        color = Color.Black

                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { /* Handle accept */ },
                        colors = ButtonDefaults.buttonColors(
                            // containerColor = Color.Green
                            containerColor = Color(0xFF34A239)
                        ), // Set background color of accept button to green
                        modifier = Modifier.padding(end = 16.dp) // Add padding to separate buttons
                    ) {
                        Text(text = "Accept")
                    }
                    Button(
                        onClick = { /* Handle decline */ },
                        colors = ButtonDefaults.buttonColors(
                            // containerColor = Color.Red
                            containerColor = Color(0xFFCF1A2F)
                        ), // Set background color of decline button to red
                        modifier = Modifier.padding(end = 16.dp) // Add padding to separate buttons
                    ) {
                        Text(text = "Decline")
                    }
                }
            }
        }
    }
}




val imageResourceIds = listOf(
    R.drawable._1,
    R.drawable.adam,
    R.drawable.gg,
    // Add more image resource IDs as needed
)

val requests = listOf(
    Request("Mohamed", "Apartment Cleaning", 3000, "DZD", "02.02.2024", imageResourceIds[0]),
    Request("Sarah", "Electrical help", 12000, "DZD", "16.05.2024", imageResourceIds[1]),
    Request("Ahmed", "Home repairs", 5000, "DZD", "18.05.2024", imageResourceIds[2]),
    Request("issam", "Painting", 9200, "DZD", "12.05.2024", imageResourceIds[0]),
    Request("Rosa", "Electrical help", 40100, "DZD", "10.03.2024", imageResourceIds[1]),
    Request("Ahmed", "Some service", 3000, "DZD", "08.09.2024", imageResourceIds[2]),
    Request("Mohamed", "Apartment Cleaning", 3000, "DZD", "11.05.2024", imageResourceIds[0])
    // Add more requests as needed, making sure to match them with corresponding image resource IDs
)


@Preview(showBackground = true)
@Composable
fun RequestScreen() {
    Column {
        requests.forEach { request ->
            RequestItem(request = request)
        }
    }
}
