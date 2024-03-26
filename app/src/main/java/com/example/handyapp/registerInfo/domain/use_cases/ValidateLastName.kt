package com.example.handyapp.registerInfo.domain.use_cases

import com.example.handyapp.register.domain.use_case.ValidationResult

class ValidateLastName {
    fun excute(lastName: String): ValidationResult {
        if(lastName.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "empty last name"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}