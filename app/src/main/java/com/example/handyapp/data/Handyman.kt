package com.example.handyapp.data

data class Handyman(
    val firstName: String? = null,
    val lastName: String? = null,
    val phoneNumber: String? = null,
    val wilaya: String? = null,
    val city: String? = null,
    val street: String? = null,
    val day: String? = null,
    val month: String? = null,
    val year: String? = null,
    val serviceCat: String?  = null,
    val status: String? = "NEW"
)