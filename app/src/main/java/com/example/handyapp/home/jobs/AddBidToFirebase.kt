package com.example.handyapp.home.jobs

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

fun addBid(bid:ABid, jobId: String){
    val db = Firebase.firestore
    val jobRef = db.collection("Jobs").document(jobId)
    val currentUser = FirebaseAuth.getInstance().currentUser


    if (currentUser != null) {
        // Get the user's ID
        val userId = currentUser.uid
        jobRef.collection("bids")
            .add(
                hashMapOf(
                    "handymandID" to userId,
                    "description" to bid.Description,
                    "price" to bid.price,
                    "time" to bid.day,
                )
            )
            .addOnSuccessListener { documentReference ->
                println("DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                println("Error adding document: $e")
            }
    } else {
        println("No user is currently signed in.")
    }
}