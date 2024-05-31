package com.example.handyapp.domain.model

data class ProfileHandyMenInfo (
    val nbReviews : Double = 0.0,
    val Rating : Double = 0.0,
    val OrdersCompleted : String = "",
    val About : String = "",
    val SubCategory : String = "",//List<String> = emptyList(),
    val WorkingAreas : String = ""// List<String> = emptyList(),
)