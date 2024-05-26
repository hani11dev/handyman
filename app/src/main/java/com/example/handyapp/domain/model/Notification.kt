package com.example.handyapp.domain.model

import com.google.firebase.Timestamp

data class Notification(
    val title : String= "",
    val text : String = "",
    val sender : String = "",
    val receiver : String = "",
    val deepLink : String = "",
    val time : Timestamp = Timestamp.now(),
)