package com.example.handyapp

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class LoginAppFlow (): Application(){
override fun onCreate(){
    super.onCreate()
    FirebaseApp.initializeApp(this)
}
}