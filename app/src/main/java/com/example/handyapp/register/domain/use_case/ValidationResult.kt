package com.example.handyapp.register.domain.use_case

data class ValidationResult(val successful: Boolean,
    val errorMessage: String? = null
)
