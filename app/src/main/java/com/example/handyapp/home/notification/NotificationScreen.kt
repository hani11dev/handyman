//package com.example.handyapp.home.notification
//
////import coil.compose.AsyncImage
////import com.google.android.gms.tasks.Task
//import com.google.firebase.firestore.*
////import com.google.gson.Gson
//import android.annotation.SuppressLint
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Notifications
//import androidx.compose.material3.Card
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.painter.Painter
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.rememberNavController
//import coil.compose.AsyncImage
//import com.google.android.datatransport.cct.internal.LogResponse.fromJson
//import com.google.android.gms.tasks.Task
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.QuerySnapshot
////import com.google.gson.Gson
//
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@SuppressLint("SuspiciousIndentation", "UnusedMaterial3ScaffoldPaddingParameter")
//@Composable
//fun NotificationScreen(navController: NavHostController) {
//    Scaffold(topBar = {
//        androidx.compose.material3.TopAppBar(
//            title = {
//                Text(
//                    text = "Notification",
//                    //fontFamily = PoppinsFontFamily,
//                    fontWeight = FontWeight.SemiBold
//                )
//            },
//            navigationIcon = {
//                IconButton(onClick = { navController.navigateUp() }) {
//                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
//                }
//            }
//        )
//    }) {
//
//        Column(modifier = Modifier.padding(top = 48.dp)) {
//
//            // this like top bar, so we do it on scaffold
//
//            /*Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(20.dp), horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(text = "NOTIFICATIONS", style = MaterialTheme.typography.titleLarge)
//                Spacer(modifier = Modifier.size(20.dp))
//                Icon(
//                    imageVector =
//                    Icons.Default.Notifications, contentDescription = ""
//                )
//            }*/
//            val data = listOf(
//                Notifications(
//                    senderId = "client",
//                    receiverId = "Pay Handymane",
//                    title = "Affinity Home Solution LLC",
//                    subtitle = "When are you availble ?",
//                    time = "7:15 PM"
//                ),
//                Notifications(
//                    senderId = "client",
//                    receiverId = "handymane",
//                    title ="Pay Handymane",
//                    subtitle = "Thanks for contacting us.",
//                    time ="6:00 AM"
//                ),
//                Notifications(
//                    senderId = "client",
//                    receiverId = "handymane",
//                    title = "Home123",
//                    subtitle = "Welcome !",
//                    time="12 :34 PM"
//                ), Notifications(
//                    senderId = "client",
//                    receiverId = "handymane",
//                    title = "Handymane",
//                    subtitle = "when re u available , please !",
//                    time = "12:00 pm"
//                ), Notifications(
//                    senderId = "client",
//                    receiverId = "handymane",
//                    title = "Homyyy5",
//                    subtitle = "where re you !",
//                    time="3:00 PM"
//                ),Notifications(
//                    senderId = "client",
//                    receiverId = "handymane",
//                    title = "Home1234",
//                    subtitle = "Thank you .",
//                    time ="7:40 AM"
//                ), Notifications(
//                    senderId = "client",
//                    receiverId = "handymane",
//                    title = "Handymane",
//                    subtitle = "Thank you .",
//                    time ="8 : 00 AM"
//                )
//            )
//            LazyColumn {
//                items(data) { Notifications ->
//                    MyCard(notif = Notifications)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun MyCard(notif: Notifications) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(96.dp)
//            .padding(1.dp),
//
//        shape = RoundedCornerShape(5),
//    ) {
//        Row(modifier = Modifier.padding(12.dp)) {
//            AsyncImage(
//                model = "https://th.bing.com/th/id/OIP.e1KNYwnuhNwNj7_-98yTRwHaF7?rs=1&pid=ImgDetMain",
//                contentDescription = "Notification Image",
//                modifier = Modifier
//                    .width(70.dp)
//                    .height(70.dp)
//                    .clip(CircleShape),
//                contentScale = ContentScale.Crop
//            )
//            Row {
//                Column(modifier = Modifier
//                    .padding(10.dp)
//                    .fillMaxWidth()
//                    .weight(1f)) {
//                    Text(
//                        text = notif.title,
//                        style = MaterialTheme.typography.titleMedium, maxLines = 1,
//                        overflow = TextOverflow.Ellipsis
//                    )
//                    Text(
//                        text = notif.subtitle,
//                        overflow = TextOverflow.Ellipsis,
//                        lineHeight = 12.sp
//                    )
//                }
//                Text(text = notif.time, modifier = Modifier.padding(10.dp))
//            }
//        }
//    }
//}
//
////
////data class Notifications(
////    val senderId: String,
////    val receiverId: String,
////    val title: String,
////    val subtitle: String,
////    val time: String
////) {
////    companion object {
////        private val firestore = FirebaseFirestore.getInstance()
////       // private val gson = Gson()
////
////       fun fromJson(json: String): Notifications {
////            return gson.fromJson(json, Notifications::class.java)
////        }
////
////        fun getNotificationsList(
////            collectionPath: String,
////            userId: String,
////            listener: (List<Notifications>) -> Unit
////        ): ListenerRegistration {
////            return firestore.collection(collectionPath)
////                .whereEqualTo("senderId", userId) // Filter documents by senderId
////                .addSnapshotListener { value, error ->
////                    if (error != null) {
////                        // Handle error
////                        throw error
////                    }
////
////                    val notificationsList = mutableListOf<Notifications>()
////                    value?.forEach { documentSnapshot ->
////                        val notificationJson = documentSnapshot.getString("notificationJson")
////                        notificationJson?.let {
////                            val notification = fromJson(it)
////                            notificationsList.add(notification)
////                        }
////                    }
////                    listener(notificationsList.toList())
////                }
////        }
////    }
////}