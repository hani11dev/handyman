package com.example.handyapp.data

data class Handyman(
    val FirstName: String? = null,
    val LastName: String? = null,
    val phoneNumber: String? = null,
    val wilaya: String? = null,
    val city: String? = null,
    val street: String? = null,
    val day: String? = null,
    val month: String? = null,
    val year: String? = null,
    val serviceCat: String?  = null,
    val status: String? = "NEW",
    val averageSalary: Int? = null,
    val about: String? = null,
    val ordersCompleted: Int = 0,
    val nbReviews: String = "0"
)