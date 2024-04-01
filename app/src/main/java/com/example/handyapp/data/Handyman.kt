package com.example.handyapp.data

data class Handyman(
    val FirstName: String? = null,
    val LastName: String? = null,
    val phoneNumber: String? = null,
    val Wilaya: String? = null,
    val City: String? = null,
    val Street: String? = null,
    val Day: String? = null,
    val Month: String? = null,
    val Year: String? = null,
    val Category: String? = null,
    val SubCategory: ArrayList<String> = ArrayList(),
    val status: String? = "NEW",
    val AverageSalary: Int? = null,
    val About: String? = null,
    val OrdersCompleted: String = "0",
    val nbReviews: String = "0"
)