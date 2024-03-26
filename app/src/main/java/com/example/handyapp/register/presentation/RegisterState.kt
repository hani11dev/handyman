package com.example.handyapp.register.presentation

data class RegisterState(
    val firstName: String = "",
    val firstNameError: String? = null,
    val lastName: String = "",
    val lastNameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val phoneNumber: String = "",
    val phoneNumberError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    val confirmed: Boolean = false,
    val confirmedError: String? = null,
    val isLoading: Boolean = false,
    val isSuccessful: Boolean = false,
    val isError: Boolean =false
){}