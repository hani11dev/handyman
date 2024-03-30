package com.example.handyapp.finalRegister

data class FinalRegisterState (
    val about: String = "",
    val aboutError: String? = null,
    val averageSalary: String = "",
    val averageSalaryError: String? = null,
    val wilaya: String = "",
    val wilayaError: String? = null,
    val city: String = "",
    val cityError: String? = null,
    val street: String = "",
    val streetError: String? = null,
    val workingAreas: String = "",
    val workingAreasError: String? = null
)