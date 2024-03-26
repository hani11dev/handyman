package com.example.handyapp.registerInfo.data

data class userInfo(
    val firstName: String = "",
    val lastName: String = "",
    val day: String = "",
    val month: String = "",
    val year: String = "",
    val wilaya: String  = "",
    val city: String = "",
    val street: String = "",
    val isAdminAccepted: Boolean = false,
){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "wilaya" to wilaya,
            "city" to city,
            "street" to street,
            "day" to day,
            "month" to month,
            "year" to year
        )
    }
}