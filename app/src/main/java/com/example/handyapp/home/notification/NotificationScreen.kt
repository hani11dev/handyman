//package com.example.handyapp.home.notification
//import coil.compose.AsyncImage
//import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
//import com.google.gson.Gson
import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.handyapp.Response
import com.example.handyapp.domain.model.Notification
import com.example.handyapp.home.notification.NotificationsViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import com.example.handyapp.R


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SuspiciousIndentation", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotificationScreen(navController: NavHostController , viewModel : NotificationsViewModel = hiltViewModel() , context : Context = LocalContext.current) {

    when(val resp = viewModel.notificationList.value){
        is Response.onLoading -> {
            CircularProgressIndicator()
        }
        is Response.onFaillure -> {
            Toast.makeText(context , resp.message , Toast.LENGTH_SHORT).show()
        }
        is Response.onSuccess -> {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(resp.data.sortedByDescending { it.time }) { notification ->
                    MyCard(notif = notification)
                    Divider(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp))
                }
            }
        }
    }
    /*Scaffold(topBar = {
        androidx.compose.material3.TopAppBar(
            title = {
                Text(
                    text = "Notification",
                    //fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                }
            }, //backgroundColor = MaterialTheme.colorScheme.background
        )
    }) {

        Column(modifier = Modifier.padding(top = 48.dp)) {

            // this like top bar, so we do it on scaffold

            *//*Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "NOTIFICATIONS", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.size(20.dp))
                Icon(
                    imageVector =
                    Icons.Default.Notifications, contentDescription = ""
                )
            }*//*
            val data = listOf(
                Notifications(
                    senderId = "client",
                    receiverId = "Pay Handymane",
                    title = "Affinity Jobs Solution LLC",
                    subtitle = "When are you availble ?",
                    time = "7:15 PM"
                ),
                Notifications(
                    senderId = "client",
                    receiverId = "handymane",
                    title ="Pay Handymane",
                    subtitle = "Thanks for contacting us.",
                    time ="6:00 AM"
                ),
                Notifications(
                    senderId = "client",
                    receiverId = "handymane",
                    title = "Home123",
                    subtitle = "Welcome !",
                    time="12 :34 PM"
                ), Notifications(
                    senderId = "client",
                    receiverId = "handymane",
                    title = "Handymane",
                    subtitle = "when re u available , please !",
                    time = "12:00 pm"
                ), Notifications(
                    senderId = "client",
                    receiverId = "handymane",
                    title = "Homyyy5",
                    subtitle = "where re you !",
                    time="3:00 PM"
                ),Notifications(
                    senderId = "client",
                    receiverId = "handymane",
                    title = "Home1234",
                    subtitle = "Thank you .",
                    time ="7:40 AM"
                ), Notifications(
                    senderId = "client",
                    receiverId = "handymane",
                    title = "Handymane",
                    subtitle = "Thank you .",
                    time ="8 : 00 AM"
                )
            )
            LazyColumn {
                items(data) { Notifications ->
                    MyCard(notif = Notifications)
                }
            }
        }
    }*/
}

@Composable
fun MyCard(notif: Notification) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(5),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest)
    ) {
        Column {

            val notDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(Instant.ofEpochSecond(notif.time.seconds).epochSecond) , ZoneId.systemDefault())
            val currentDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(Instant.now().epochSecond), ZoneId.systemDefault())
            val d = Duration.between(notDate, currentDate)
            val hours = d.toHours()
            val days = d.toDays()
            val weeks = days / 7

            // Afficher le format approprié
            val displayDate = when {
                hours < 1 -> {"${d.toMinutes()}mn"}
                days < 1 -> {"${d.toHours()}h"}
                days < 7 -> "${days}d"
                weeks < 4 -> "${weeks}w"
                else -> "${weeks / 4}m" // Mois approximatifs
            }
            val s = notif.time.toDate().time

            Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                androidx.compose.material3.Icon(
                    painter = if (notif.deepLink == "request") painterResource(id = R.drawable.outlined_task_icon) else painterResource(
                        id = R.drawable.outlined_request_icon
                    ), contentDescription = null, modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                /*Image(
                    painter = if (notif.deepLink == "request") painterResource(id = R.drawable.outlined_task_icon) else painterResource(
                        id = R.drawable.outlined_request_icon
                    ) ,
                    contentDescription = "Notification Image",
                    modifier = Modifier
                        .width(36.dp)
                        .height(36.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )*/
                Row {
                    Column(
                        modifier = Modifier

                            .fillMaxWidth()
                            .weight(1f)
                    ) {

                        Text(
                            text = notif.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(text = buildAnnotatedString {
                            append(notif.text)
                            withStyle(SpanStyle(fontSize = 12.sp , fontWeight = FontWeight.SemiBold , color = Color.Gray)){
                                append(".$displayDate")
                            }
                        } , overflow = TextOverflow.Ellipsis,
                            lineHeight = 12.sp,
                            fontSize = 14.sp,
                            maxLines = 2)
                        /*Text(
                            text = notif.text + " $displayDate",
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 12.sp,
                            fontSize = 14.sp,
                            maxLines = 2,
                        )*/

                    }

                }
            }

        }
    }
}


data class Notifications(
    val senderId: String,
    val receiverId: String,
    val title: String,
    val subtitle: String,
    val time: String
) {
    companion object {
        private val firestore = FirebaseFirestore.getInstance()
        private val gson = Gson()

        fun fromJson(json: String): Notifications {
            return gson.fromJson(json, Notifications::class.java)
        }

        fun getNotificationsList(
            collectionPath: String,
            userId: String,
            listener: (List<Notifications>) -> Unit
        ): ListenerRegistration {
            return firestore.collection(collectionPath)
                .whereEqualTo("senderId", userId) // Filter documents by senderId
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        // Handle error
                        throw error
                    }

                    val notificationsList = mutableListOf<Notifications>()
                    value?.forEach { documentSnapshot ->
                        val notificationJson = documentSnapshot.getString("notificationJson")
                        notificationJson?.let {
                            val notification = fromJson(it)
                            notificationsList.add(notification)
                        }
                    }
                    listener(notificationsList.toList())
                }
        }
    }
}